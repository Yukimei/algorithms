package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;


import formbean.CreateCustomerForm;
import model.CustomerDAO;
import model.Model;

public class CreateCustomerAction extends Action {
	private FormBeanFactory<CreateCustomerForm> formBeanFactory = FormBeanFactory.getInstance(CreateCustomerForm.class);

	private CustomerDAO customerDAO;

	public CreateCustomerAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "create_customer.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		HttpSession session = request.getSession();

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			errors.add("Only employees can create accounts");
			return "view-account.jsp";
		}

		try {

			CreateCustomerForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			if (!form.isPresent()) {
				System.out.println("not present");
				return "create-customer.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "create-customer.jsp";
			}

			if (customerDAO.read(form.getUserName()) != null) {
				errors.add("This username " + form.getUserName() + " has already been registered");

				return "create-customer.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {

				return "create-customer.jsp";
			}


			int flag = customerDAO.createCustomer(form.getUserName(), form.getFirstName(), form.getLastName(),
					form.getPassword(), form.getAddrLine1(), form.getAddrLine2(), form.getCity(), form.getState(),
					form.getZip());

			if (flag == -1) {
				errors.add("This username " + form.getUserName() + " has already been registered");
				return "create-customer.jsp";
			}

			if (flag == 1) {

				request.setAttribute("customermessage", "Customer " + form.getUserName() + " has been created");
				request.removeAttribute("form");
			}


			return "create-customer.jsp";

		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
