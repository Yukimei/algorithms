package controller;

import java.io.IOException;
import java.io.StringWriter;
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
import databean.TransactionBean;
import formbean.RequestCheckForm;
import model.CustomerDAO;
import model.Model;
import model.TransactionDAO;

public class RequestCheckAction extends Action {
	private FormBeanFactory<RequestCheckForm> formBeanFactory = FormBeanFactory.getInstance(RequestCheckForm.class);

	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;

	public RequestCheckAction(Model model) {
		customerDAO = model.getCustomerDAO();
		transactionDAO = model.getTransactionDAO();
	}

	public String getName() {
		return "requestCheck";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		JSONObject error = new JSONObject();
		JSONObject success = new JSONObject();
		HttpSession session = request.getSession();

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");

		if (!isCustomer) {
			errors.add("You are not eligible to request check for a customer");
			error.put("Message", "I'm sorry you are not authorized to perform that action.");
			// return "customer-list.jsp?page=1";
			return error.toJSONString();
		}

		try {
			Customer customer = (Customer) session.getAttribute("customer");
			RequestCheckForm form = formBeanFactory.create(request);

			if (customer == null) {
				error.put("Message", "You must log in prior to making this request.");
				return error.toJSONString();
			}

			if (!form.isPresent()) {
				System.out.println(form.isPresent());
				error.put("Message", "You must log in prior to making this request.");
				return error.toJSONString();
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				error.put("Message", "Sorry there're validation errors.");
				return error.toJSONString();
			}

			customer = customerDAO.read(customer.getUserName());

			double amount = Double.parseDouble(form.getCashValue());
			long amountLong = (long) (amount * 100);
			AmountHelper amountHelper = new AmountHelper(amountLong);

			int flag = customerDAO.updateAvailableCash(customer.getUserName(), amountHelper);

			if (flag == -1) {
				error.put("Message", "I'm sorry, the amount requested is greater than the balance of your account");
				return error.toJSONString();
			} else {
				TransactionBean transbean = new TransactionBean();
				transbean.setCustomerId(customer.getCustomerId());
				transbean.setAmount(amountLong);
				transbean.setTransactionType("Request");
				transactionDAO.createAutoIncrement(transbean);
				customerDAO.doTransactionHelper(transactionDAO);
				success.put("Message", "The withdrawal was successfully completed");
				StringWriter sw = new StringWriter();
				try {
					success.writeJSONString(sw);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return sw.toString();
			}
		} catch (RollbackException e) {
			errors.add("As your transaction is still in process, please try again later.");
			return "request-check.jsp";
		} catch (FormBeanException e) {
			errors.add("Input format error");
			return "request-check.jsp";
		}
	}
}
