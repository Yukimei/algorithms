package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import databean.TransitionDayBean;
import model.Model;

public class FundListTurnPageAction extends Action {

	// private FormBeanFactory<SearchForm> formBeanFactory =
	// FormBeanFactory.getInstance(SearchForm.class);
	private Model model;

	public FundListTurnPageAction(Model model) {
		this.model = model;
	}

	public String getName() {
		return "FundListTurnPage.do";
	}

	public String perform(HttpServletRequest request) {

		HttpSession session = request.getSession();

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (!isCustomer) {
			errors.add("Sorry, you are not authorized to access this information.");
			return "customer-list.jsp?page=1";
		}
		try {
			TransitionDayBean[] fundList = model.getFundList();
			request.getSession(true).setAttribute("fundList", fundList);
			request.getSession(true).setAttribute("size", fundList.length);

			int size = (int) session.getAttribute("size");

			String pageString = request.getParameter("page");

			if (pageString == null || pageString.length() == 0) {
				return "research-fund.jsp?page=1";
			}

			try {
				Integer.parseInt(pageString);
			} catch (NumberFormatException e) {
				errors.add("Page number is not an integer.");
				return "research-fund.jsp?page=1";
			}

			int page = Integer.parseInt(pageString);
			if ((page - 1) * 10 >= size) {
				errors.add("The number of page is incorrect.");
				return "research-fund.jsp?page=1";
			}

			request.setAttribute("page", page + 1);

			return "research-fund.jsp?page=" + page;
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
