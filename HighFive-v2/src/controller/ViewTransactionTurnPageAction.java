package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import formbean.IdForm;
import model.CustomerDAO;
import model.Model;

public class ViewTransactionTurnPageAction extends Action {
	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);
	
	private CustomerDAO customerDAO;
	private Model model;

	public ViewTransactionTurnPageAction(Model model) {
		customerDAO = model.getCustomerDAO();
		this.model = model;
	}

	public String getName() {
		customerDAO = model.getCustomerDAO();
		return "ViewTransactionTurnPage.do";
	}

	public String perform(HttpServletRequest request) {

		HttpSession session = request.getSession();

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			int id = 0;
			Customer customer = null;
			Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
			if (isCustomer) {
				Customer c = (Customer) session.getAttribute("customer");
				if (c != null) {
					id = c.getCustomerId();
					customer = customerDAO.read(id);
				} else {
					return "login-customer.jsp";
				}
			} else {
				IdForm form = formBeanFactory.create(request);
				errors.addAll(form.getValidationErrors());
				if (errors.size() != 0) {
					return "customer-list.jsp?page=1";
				}

				id = form.getIdAsInt();
				customer = customerDAO.read(id);
				if (customer == null) {
					errors.add("Customer with ID " + id + " does not exist.");
					return "customer-list.jsp?page=1";
				}
			}
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			System.out.println(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}

		int size = (int) session.getAttribute("size");

		String pageString = request.getParameter("page");

		if (pageString == null || pageString.length() == 0) {
			return "transaction-history.jsp?page=1";
		}

		try {
			Integer.parseInt(pageString);
		} catch (NumberFormatException e) {
			errors.add("Page number is not an integer.");
			return "transaction-history.jsp?page=1";
		}

		int page = Integer.parseInt(pageString);
		if ((page - 1) * 10 >= size) {
			errors.add("The number of page is incorrect.");
			return "transaction-history.jsp?page=1";
		}

		request.setAttribute("page", page + 1);

		return "transaction-history.jsp?page=" + page;
	}
}
