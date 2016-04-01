package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import formbean.SearchCustomerForm;
import model.CustomerDAO;
import model.Model;

public class ViewCustomerAction extends Action {
	private FormBeanFactory<SearchCustomerForm> formBeanFactory = FormBeanFactory.getInstance(SearchCustomerForm.class);

	// private FavoriteDAO favoriteDAO;
	private CustomerDAO customerDAO;

	public ViewCustomerAction(Model model) {
		customerDAO = model.getCustomerDAO();
		// userDAO = model.getUserDAO();
	}

	public String getName() {
		return "customer_list.do";
	}

	public String perform(HttpServletRequest request) {

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		HttpSession session = request.getSession();

		try {
			boolean isCustomer = (boolean) session.getAttribute("isCustomer");
			if (isCustomer) {
				errors.add("Customers can not access the customer list");
				return "view-account.jsp";
			}

			SearchCustomerForm form;

			form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			Customer[] customers = customerDAO.getAllCustomers();
			session.setAttribute("size", customers.length);
			session.setAttribute("customerList", customers); // ??

			if (!form.isPresent()) {
				return "customer-list.jsp?page=1";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "customer-list.jsp?page=1";
			}

			List<Customer> resultList = new ArrayList<Customer>();
			for (Customer customer : customers) {
				String customerName = customer.getUserName();
				String searchName = form.getSearch();
				if (customerName.equals(searchName)) {
					resultList.add(customer);
				}
			}
			if (resultList.size() == 0) {
				errors.add("The username you inputed doesn't match with any customer.");
			}

			session.setAttribute("size", resultList.size());
			session.setAttribute("customerList", resultList);

			return "customer-list.jsp?page=1";

		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}

	}
}
