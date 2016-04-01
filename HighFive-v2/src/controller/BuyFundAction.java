package controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.AmountHelper;
import databean.Customer;
import databean.TransactionBean;
import formbean.BuyFundForm;
import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.TransactionDAO;

public class BuyFundAction extends Action {
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;
	private FormBeanFactory<BuyFundForm> formBeanFactory = FormBeanFactory.getInstance(BuyFundForm.class);
	private TransactionDAO transactionDAO;

	private Model model;

	public BuyFundAction(Model model) {
		this.model = model;
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		customerDAO = model.getCustomerDAO();

	}

	public String getName() {
		return "by_fund.do";
		
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		HttpSession session = request.getSession();
		request.setAttribute("errors", errors);
		try {
		    System.out.println("haha");
			// check whether the user is customer
			boolean isCustomer = (boolean) session.getAttribute("isCustomer");

			Customer customer = (Customer) session.getAttribute("customer");

			if (!isCustomer) {
				errors.add("You are not eligible to buy fund for a customer");
				return "customer-list.jsp?page=1";
			}

			if (customer == null) {
				return "login_customer.do";
			}

			session.setAttribute("fundList", model.getFundList());

			// check the whether the form is clicked
			BuyFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			// any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			String fundId = request.getParameter("fundId");
			String dollarAmount = request.getParameter("amount");

			int id = Integer.parseInt(fundId);
			try {
				if (fundDAO.read(id) == null) {
					errors.add("Fund Id: " + id + " doesn't exist.");
				}
			} catch (RollbackException e) {
				e.printStackTrace();
			}

			if (errors.size() != 0) {
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			// buy fund action
			customer = customerDAO.read(customer.getCustomerId());
			session.setAttribute("customer", customerDAO.read(customer.getUserName()));

			double amount = Double.parseDouble(dollarAmount);
			if (amount <= 0) {
				errors.add("Sorry, purchase amount must be greater than zero.");
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			} else if (amount <= 0.01 || amount >= 100000.0) {
				errors.add("Sorry, purchase amount must be greater than 0.01 and less than 100,000.");
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			long amountLong = (long) (amount * 100);
			AmountHelper amountHelper = new AmountHelper(amountLong);
			int flag = customerDAO.updatePendingAmount(customer.getUserName(), amountHelper);
			if (flag == -1) {
				errors.add("Amount requested is greater than available amount.");
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			if (flag == 1) {
				TransactionBean transaction = new TransactionBean();
				transaction.setCustomerId(customer.getCustomerId());
				transaction.setAmount(amountLong);
				transaction.setFundId(id);
				transaction.setTransactionType("Buy");
				transactionDAO.add(transaction);
			}

			session.setAttribute("customer", customerDAO.read(customer.getUserName()));
			request.setAttribute("customer", customerDAO.read(customer.getUserName()));

			session.setAttribute("fundList", model.getFundList());

			DecimalFormat df = new DecimalFormat("#,##0.00");

			String tmp = df.format((double) (amountLong) / 100);
			System.out.println(tmp);
			request.setAttribute("message",
					"Dear " + customer.getFirstName() + ", you've purchased Fund " + id + " for $" + tmp + ".");

			if (errors.size() != 0) {
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "buy-fund.jsp";
			}

			session.setAttribute("customer", customerDAO.read(customer.getUserName()));
			return "buy-fund.jsp";
			// end

		} catch (RollbackException e) {
			errors.add("Sorry, your transaction is in pending state. Please try again later.");
			return "buy-fund.jsp";
		} catch (FormBeanException e) {
			errors.add("Did not enter valid amount");
			return "buy-fund.jsp";
		}

	}

}