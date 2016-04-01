package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import formbean.IdForm;
import model.CustomerDAO;
import model.Model;

public class ResetPwdAction extends Action {
	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);
	private CustomerDAO customerDAO;

	public ResetPwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "reset_pwd.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			HttpSession session = request.getSession();
			Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
			if (isCustomer) {
				errors.add("Sorry, you are not eligible to access this page.");
				return "view_account.do";
			}

			IdForm form = formBeanFactory.create(request);
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "customer-list.jsp?page=1";
			}

			int id = form.getIdAsInt();
			Customer customer = customerDAO.read(id);
			if (customer == null) {
				errors.add("ID " + id + " does not exist.");
				return "customer-list.jsp?page=1";
			}

			Random random = new Random();
			int s = random.nextInt(1000);
			String password = Integer.toString(s);

			customerDAO.setPwd(customer.getUserName(), password);

			request.setAttribute("message",
					"Customer " + customer.getFirstName() + "'s password has been reset as " + password);

			return "customer-list.jsp?page=1";
		} catch (RollbackException e) {
			errors.add(e.toString());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.toString());
			return "error.jsp";
		}
	}
}
