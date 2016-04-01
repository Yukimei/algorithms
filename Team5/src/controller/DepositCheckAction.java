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
import databean.Employee;
import databean.TransactionBean;
import formbean.DepositCheckForm;
import model.CustomerDAO;
import model.Model;
import model.TransactionDAO;

public class DepositCheckAction extends Action {
	private FormBeanFactory<DepositCheckForm> formBeanFactory = FormBeanFactory.getInstance(DepositCheckForm.class);
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;

	public DepositCheckAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "depositCheck";
	}

	public String perform(HttpServletRequest request) {

		JSONObject obj = new JSONObject();
		List<String> errors = new ArrayList<String>();
		HttpSession session = request.getSession();

		Customer customer = (Customer) session.getAttribute("customer");
		Employee employee = (Employee) session.getAttribute("employee");

		if (customer == null && employee == null) {
			String message = "You must log in prior to make this request";
			obj.put("message", message);
			return obj.toJSONString();
		}

		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			String message = "I’m sorry you are not authorized to perform that action";
			obj.put("message", message);
			return obj.toJSONString();
		}

		try {

			DepositCheckForm depositForm = formBeanFactory.create(request);

			if (!depositForm.isPresent()) {
				String message = "“I’m sorry, there was a problem depositing the money";
				obj.put("message", message);
				return obj.toJSONString();
			}

			errors.addAll(depositForm.getValidationErrors());
			if (errors.size() != 0) {
				String message = "“I’m sorry, there was a problem depositing the money";
				obj.put("message", message);
				return obj.toJSONString();
			}

			customer = customerDAO.read(depositForm.getUsername());
			if (customer == null) {
				String message = "“I’m sorry, there was a problem depositing the money";
				obj.put("message", message);
				return obj.toJSONString();
			}

			double amountDouble = Double.parseDouble(depositForm.getCash());
			if (amountDouble <= 0) {
				String message = "“I’m sorry, there was a problem depositing the money";
				obj.put("message", message);
				return obj.toJSONString();
			}

			TransactionBean bean = new TransactionBean();
			bean.setAmount((long) (amountDouble * 100));
			bean.setCustomerId(customer.getCustomerId());
			bean.setTransactionType("Deposit");
			transactionDAO.createAutoIncrement(bean);
			//new
			customerDAO.doTransactionHelper(transactionDAO);
			// 传入用户ID，交多少钱
			// 返回

			String message = "The account has been successfully updated";
			obj.put("message", message);
			return obj.toJSONString();
		} catch (RollbackException e) {
			errors.add(e.toString());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.toString());
			return "error.jsp";
		}
	}
}
