package controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.TransactionBean;
import formbean.DepositCheckForm;
import formbean.IdForm;
import model.CustomerDAO;
import model.Model;
import model.TransactionDAO;

public class DepositCheckAction extends Action {
	private FormBeanFactory<IdForm> idFormBean = FormBeanFactory.getInstance(IdForm.class);
	private FormBeanFactory<DepositCheckForm> formBeanFactory = FormBeanFactory.getInstance(DepositCheckForm.class);
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;

	public DepositCheckAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "deposit_check.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		HttpSession session = request.getSession();
		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			errors.add("You have to be an employee to deposit a check.");
			return "view-account.jsp";
		}
		Customer customer = (Customer) session.getAttribute("customer");

		try {
			if (customer == null) {
				IdForm idForm = idFormBean.create(request);
				int id = idForm.getIdAsInt();

				errors.addAll(idForm.getValidationErrors());
				if (errors.size() != 0) {
					return "customer-list.jsp?page=1";
				}

				customer = customerDAO.read(id);
				if (customer == null) {
					errors.add("There is no customer whose ID is" + id);
					return "customer-list.jsp?page=1";
				}

				session.setAttribute("customer", customer);
			}

			DepositCheckForm depositForm = formBeanFactory.create(request);

			if (!depositForm.isPresent()) {
				return "deposit-check.jsp";
			}

			errors.addAll(depositForm.getValidationErrors());
			if (errors.size() != 0) {
				return "deposit-check.jsp";
			}

			double amountDouble = Double.parseDouble(depositForm.getAmount());
			if (amountDouble <= 0) {
				errors.add("Deposit amount must be a postive number.");
				return "deposit-check.jsp";
			}
			if (amountDouble <= 0.01) {
				errors.add("Deposit amount must not be larger than $0.01.");
				return "deposit-check.jsp";
			}

			if (amountDouble > 1000000) {
				errors.add("Deposit amount have to be no larger than $1 million");
				return "deposit-check.jsp";
			}

			TransactionBean bean = new TransactionBean();
			bean.setAmount((long) (amountDouble * 100));
			bean.setCustomerId(customer.getCustomerId());
			bean.setTransactionType("Deposit Check");
			transactionDAO.add(bean);

			// 传入用户ID，交多少钱
			// 返回

			DecimalFormat format = new DecimalFormat("#,##0.00");
			String amount = format.format((double) ((long) (amountDouble * 100)) / 100);

			request.setAttribute("message",
					"$" + amount + " is now pending for " + customer.getUserName() + "'s account");

			return "deposit-check.jsp";
		} catch (RollbackException e) {
			errors.add(e.toString());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.toString());
			return "error.jsp";
		}
	}
}
