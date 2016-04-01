package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import databean.Customer;
import model.CustomerDAO;
import model.Model;

public class CustomerListTurnPageAction extends Action {

	private CustomerDAO customerDAO;

	public CustomerListTurnPageAction(Model model) {
		customerDAO = model.getCustomerDAO();

	}

	public String getName() {
		return "CustomerListTurnPage.do";
	}

	public String perform(HttpServletRequest request) {
		System.out.println("sd");

		HttpSession session = request.getSession();

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			errors.add("Customers can not access the customerList");
			return "view-account.jsp";
		}
		try {
			Customer[] customers = customerDAO.getAllCustomers();
			session.setAttribute("size", customers.length);
			session.setAttribute("customerList", customers);

			int size = (int) session.getAttribute("size");

			// PageForm pageForm = formBeanFactory.create(request);
			String pageString = request.getParameter("page");

			if (pageString == null || pageString.length() == 0) {
				return "customer-list.jsp?page=1";
			}

			try {
				Integer.parseInt(pageString);
			} catch (NumberFormatException e) {
				errors.add("Page number must be an integer.");
				return "customer-list.jsp?page=1";
			}

			int page = Integer.parseInt(pageString);
			if ((page - 1) * 10 >= size) {
				errors.add("The number of page is incorrect.");
				return "customer-list.jsp?page=1";
			}

			request.setAttribute("page", page + 1);

			return "customer-list.jsp?page=" + page;
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
