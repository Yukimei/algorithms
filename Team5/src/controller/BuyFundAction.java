package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONObject;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.AmountHelper;
import databean.Customer;
import formbean.BuyFundForm;
import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;

public class BuyFundAction extends Action {
	private CustomerDAO customerDAO;
	private PositionDAO positionDAO;
	private FundDAO fundDAO;
	private FormBeanFactory<BuyFundForm> formBeanFactory = FormBeanFactory.getInstance(BuyFundForm.class);
	private TransactionDAO transactionDAO;

	private Model model;

	public BuyFundAction(Model model) {
		this.model = model;
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();

	}

	public String getName() {
		return "buyFund";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		HttpSession session = request.getSession();
		JSONObject obj = new JSONObject();

		request.setAttribute("errors", errors);
		try {

			// check whether the user is customer
			boolean isCustomer = (boolean) session.getAttribute("isCustomer");

			Customer customer = (Customer) session.getAttribute("customer");

			if (!isCustomer) {
				String message = "I’m sorry you are not authorized to perform that action";
				obj.put("message", message);
				return obj.toJSONString();
			}

			if (customer == null) {
				// return "login_customer.do";
				String message = "You must log in prior to make this request";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// check the whether the form is clicked
			BuyFundForm form = formBeanFactory.create(request);
			// request.setAttribute("form", form);

			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				String message = "“I’m sorry, there was a problem purchasing the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				System.out.println(errors);
				String message = "“I’m sorry, there was a problem purchasing the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// String fundId = request.getParameter("fundId");
			// String dollarAmount = request.getParameter("amount");
			String cashValue = form.getcashValue();
			String fundSymbol = form.getfundSymbol();
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

			if (errors.size() != 0) {
				System.out.println(errors);
				String message = "I’m sorry, there was a problem purchasing the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			// buy fund action
			customer = customerDAO.read(customer.getCustomerId());

			double amount = Double.parseDouble(cashValue);
			if (amount <= 0) {
				String message = "Sorry, purchase amount must be greater than zero.";
				obj.put("message", message);
				return obj.toJSONString();
			}

			long amountLong = (long) (amount * 100);
			AmountHelper amountHelper = new AmountHelper(amountLong);

			int flag = model.buyFundHelper(customer.getUserName(), amountHelper, fundSymbol);
			if (flag == 1) {
				String message = "The purchase was successfully completed";
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				obj.put("message", message);
				return obj.toJSONString();
			}
			session.setAttribute("customer", customerDAO.read(customer.getUserName()));
			String message = "I'm sorry, you must first deposit sufficient funds in your account in order to make this purchase";
			obj.put("message", message);
			return obj.toJSONString();

		} catch (RollbackException e) {
			errors.add("Sorry, your transaction is in pending state. Please try again later.");
			e.printStackTrace();
			return "pending";
		} catch (FormBeanException e) {
			errors.add("Did not enter valid amount");
			return "form bean exp";
		}
	}
}