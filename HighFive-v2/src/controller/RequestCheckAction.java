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
		return "request_check.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			HttpSession session = request.getSession();

			boolean isCustomer = (boolean) session.getAttribute("isCustomer");
			if (!isCustomer) {
				errors.add("You are not eligible to request check for a customer");
				return "customer-list.jsp?page=1";

			}

			RequestCheckForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			Customer customer = (Customer) session.getAttribute("customer");

			if (customer == null) {
				return "login_customer.do";
			}

			customer = customerDAO.read(customer.getUserName());
			session.setAttribute("customer", customer);

			if (!form.isPresent()) {
				return "request-check.jsp";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "request-check.jsp";
			}

			// Customer newCustomer =
			// customerDAO.getCustomerById(customer.getCustomerId());
			// long pendingBalanceLang = newCustomer.getPendingCash();
			// double pendingBalanceDouble = (double)
			// newCustomer.getPendingCash() / 100;
			double chequeAmount = Double.parseDouble(form.getAmount());

			long amountLong = (long) (chequeAmount * 100);
			AmountHelper amountHelper = new AmountHelper(amountLong);
			int flag = customerDAO.updatePendingAmount(customer.getUserName(), amountHelper);

			if (flag == -1) {
				errors.add("Amount requested exceeds pending balance, please try again.");
				session.setAttribute("customer", customerDAO.read(customer.getUserName()));
				return "request-check.jsp";
			}

			if (flag == 1) {
				TransactionBean transbean = new TransactionBean();
				transbean.setAmount((long) (chequeAmount * 100));
				transbean.setCustomerId(customer.getCustomerId());
				transbean.setTransactionType("Request Check");
				transactionDAO.add(transbean);
			}

			Customer updatedCustomer = customerDAO.read(customer.getUserName());
			session.setAttribute("customer", updatedCustomer);

			DecimalFormat format = new DecimalFormat("#,##0.00");
			String tmp = format.format((double) ((long) (chequeAmount * 100)) / 100);
			request.setAttribute("message",
					"Dear " + customer.getFirstName() + ", $ " + tmp + " has been added to your pending transaction.");

			return "request-check.jsp";
		} catch (RollbackException e) {
			errors.add("As your transaction is still in process, please try again later.");
			return "request-check.jsp";
		} catch (FormBeanException e) {
			errors.add("Input format error");
			return "request-check.jsp";
		}
	}
}
