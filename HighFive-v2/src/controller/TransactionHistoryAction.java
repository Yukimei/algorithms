package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.TransHistoryBean;
import formbean.IdForm;
import formbean.SearchForm;
import model.CustomerDAO;
import model.Model;

public class TransactionHistoryAction extends Action {
	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);
	private FormBeanFactory<SearchForm> searchFormBeanFactory = FormBeanFactory.getInstance(SearchForm.class);
	private CustomerDAO customerDAO;
	private Model model;

	public TransactionHistoryAction(Model model) {
		customerDAO = model.getCustomerDAO();
		this.model = model;
	}

	public String getName() {
		return "trans_his.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			HttpSession session = request.getSession();
			boolean isCustomer = (boolean) session.getAttribute("isCustomer");
			Customer customer = (Customer) session.getAttribute("customer");

			// if is customer, there should be related information set in the
			// session.
			if (isCustomer) {
				if (customer == null) {
					errors.add("No account is login");
					return "login-customer.jsp";
				}
			} else {
				customer = (Customer) session.getAttribute("customer");
				if (customer == null) {
					IdForm form = formBeanFactory.create(request);
					customer = customerDAO.read(form.getIdAsInt());

					errors.addAll(form.getValidationErrors());

					if (errors.size() != 0) {
						return "customer-list.jsp?page=1";
					}

					if (customer == null) {
						errors.add("Customer with ID " + request.getParameter("id") + " does not exist.");
						return "customer-list.jsp?page=1";
					}

					session.setAttribute("customer", customer);
				}
			}

			customer = (Customer) session.getAttribute("customer");

			int customerId = customer.getCustomerId();
			TransHistoryBean[] transactionHistory = model.getTransactionList(customerId);
			session.setAttribute("size", transactionHistory.length);
			session.setAttribute("allTrans", transactionHistory);

			SearchForm searchForm = searchFormBeanFactory.create(request);
			request.setAttribute("form", searchForm);

			if (!searchForm.isPresent()) {
				return "transaction-history.jsp?page=1";
			}

			errors.addAll(searchForm.getValidationErrors());
			if (errors.size() != 0) {
				return "transaction-history.jsp?page=1";
			}

			List<TransHistoryBean> resultList = new ArrayList<TransHistoryBean>();
			for (TransHistoryBean historyItem : transactionHistory) {
				String historyName = historyItem.getName().toLowerCase();
				String searchName = searchForm.getSearch().toLowerCase();
				if (historyName.equals(searchName)) {
					resultList.add(historyItem);
				}
			}

			if (resultList.size() == 0) {
				errors.add("Sorry, the name you inputed doesn't match with any fund.");
			}
			if (resultList.size() > 10) {
				List<TransHistoryBean> latestTen = new ArrayList<TransHistoryBean>();
				for (int i = 0; i < 10; i++) {
					latestTen.add(resultList.get(i));
				}
				session.setAttribute("allTrans", latestTen);
				session.setAttribute("size", 10);
			} else {
				session.setAttribute("allTrans", resultList);
				session.setAttribute("size", resultList.size());
			}
			return "transaction-history.jsp?page=1";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
