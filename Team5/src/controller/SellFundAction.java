package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONObject;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import formbean.SellFundForm;
import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;

public class SellFundAction extends Action {
	private FormBeanFactory<SellFundForm> formBeanFactory = FormBeanFactory.getInstance(SellFundForm.class);

	private TransactionDAO transactionDAO;
	private PositionDAO positionDAO;
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private Model model;

	public SellFundAction(Model model) {
		this.model = model;
		transactionDAO = model.getTransactionDAO();
		positionDAO = model.getPositionDAO();
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
	}

	public String getName() {
		return "sellFund";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		JSONObject obj = new JSONObject();

		// check whether user is customer
		HttpSession session = request.getSession();

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");

		Customer customer = (Customer) session.getAttribute("customer");

		if (!isCustomer) {
			// errors.add("You are not eligible to buy fund for a customer");
			// return "customer-list.jsp?page=1";
			String message = "I’m sorry you are not authorized to perform that action";
			obj.put("message", message);
			return obj.toJSONString();
		}

		// check whether user is customer

		if (customer == null) {
			// return "login_customer.do";
			String message = "You must log in prior to make this request";
			obj.put("message", message);
			return obj.toJSONString();
		}

		try {
			// request.setAttribute("positionList",
			// getPositionList(customer.getCustomerId()));

			// check whether the form is clicked
			SellFundForm form = formBeanFactory.create(request);
			// request.setAttribute("form", form);

			// if no parm were passed, return with no errors so that the form
			// will be presented
			if (!form.isPresent()) {
				// return "sell-fund.jsp";
				System.out.println(2);
				String message = "“I’m sorry, there was a problem selling the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				// return "sell-fund.jsp";
				System.out.println(1);
				String message = "“I’m sorry, there was a problem selling the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// String fundId = request.getParameter("fundId");
			// String shares = request.getParameter("shares");
			String numShares = form.getNumShares();
			String fundSymbol = form.getFundSymbol();
			double shares = (double) Integer.parseInt(numShares);

			// check whether fund exist
			try {
				if (fundDAO.symbolExist(fundSymbol) == false) {
					// errors.add("Fund Id: " + id + " doesn't exist.");
					String message = "“I’m sorry, the fundSymbol dose not exist.";
					obj.put("message", message);
					return obj.toJSONString();
				}
			} catch (RollbackException e) {
				e.printStackTrace();
			}

			// check whether user owns the fund
			int id = fundDAO.read(fundSymbol).getFundId();
			// PositionBean position = positionDAO.getPendingShares(id,
			// customer.getCustomerId());

			if (positionDAO.ifOwnThisFund(id, customer.getCustomerId()) == false) {
				// errors.add("Customer does not own this Fund.");
				// return "sell-fund.jsp";
				String message = "Customer does not own this Fund.";
				obj.put("message", message);
				return obj.toJSONString();
			}

			customer = customerDAO.read(customer.getCustomerId());

			if (shares <= 0) {
				String message = "I’m sorry, there was a problem selling the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			int flag = model.sellFundHelper(id, customer.getCustomerId(), shares);
			if (flag == -1) {
				System.out.print("joke on you");
				String message = "“I’m sorry, you must first deposit sufficient funds in your account in order to make this purchase”";
				obj.put("message", message);
				return obj.toJSONString();
			} else {
				String message = "The purchase was successfully completed";
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				obj.put("message", message);
				return obj.toJSONString();
			}
		} catch (FormBeanException e) {
			errors.add("Input format error");
			return "sell-fund.jsp";
		} catch (RollbackException e) {
			errors.add("Sorry, your transaction is in pending state. Please try again later.");
			return "sell-fund.jsp";
		}
	}
}
