package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import databean.Customer;
import databean.FundBean;
import databean.PriceHistoryBean;
import model.FundDAO;
import model.Model;
import model.PriceHistoryDAO;

public class FundStatsAction extends Action {

	private PriceHistoryDAO priceHistoryDAO;
	private FundDAO fundDAO;

	public FundStatsAction(Model model) {
		priceHistoryDAO = model.getPriceHistoryDAO();
		fundDAO = model.getFundDAO();
	}

	public String getName() {
		return "stats.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		HttpSession session = request.getSession();
		boolean isCustomer = (boolean) session.getAttribute("isCustomer");

		if (!isCustomer) {
			errors.add("You are not eligible to access fund stats page");
			return "customer-list.jsp?page=1";
		}

		Customer customer = (Customer) request.getSession(false).getAttribute("customer");
		request.getSession(true).setAttribute("customer", customer);

		String fundID = request.getParameter("id");

		if (fundID == null || fundID.length() == 0) {
			errors.add("Please choose a fund.");
			return "research-fund.jsp?page=1";
		}

		int id;
		try {
			id = Integer.parseInt(fundID);
		} catch (NumberFormatException e) {
			errors.add("Please enter a valid fund");
			return "research-fund.jsp?page=1";
		}

		try {
			FundBean fund;
			fund = fundDAO.read(id);
			if (fund == null) {
				errors.add(id + "does not exist");
				return "research-fund.jsp?page=1";
			}

			request.setAttribute("fund", fund);
			PriceHistoryBean[] historyBeans = priceHistoryDAO.read(id);
			request.setAttribute("history", historyBeans);

			return "fund-stats.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
