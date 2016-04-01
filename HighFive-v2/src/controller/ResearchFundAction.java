package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.TransitionDayBean;
import formbean.SearchForm;
import model.Model;

public class ResearchFundAction extends Action {

	private FormBeanFactory<SearchForm> formBeanFactory = FormBeanFactory.getInstance(SearchForm.class);
	private Model model;

	public ResearchFundAction(Model model) {
		this.model = model;
	}

	public String getName() {
		return "research_fund.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			HttpSession session = request.getSession();
			Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");

			if (!isCustomer) {
				errors.add("Sorry, you are not eligible to access this page.");
				return "customer-list.jsp?page=1";
			}

			Customer customer = (Customer) request.getSession(false).getAttribute("customer");

			SearchForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			TransitionDayBean[] fundList = model.getFundList();
			request.getSession(true).setAttribute("fundList", fundList);
			request.getSession(true).setAttribute("size", fundList.length);
			request.getSession(true).setAttribute("customer", customer);

			if (!form.isPresent()) {
				return "research-fund.jsp?page=1";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "research-fund.jsp?page=1";
			}

			List<TransitionDayBean> resultList = new ArrayList<TransitionDayBean>();
			for (TransitionDayBean fund : fundList) {
				String fundName = fund.getName();
				String searchName = form.getSearch();
				if (fundName.equals(searchName)) {
					resultList.add(fund);
				}
			}
			if (resultList.size() == 0) {
				errors.add("Sorry, there is no such fund whose name is." + form.getSearch());
			}
			request.getSession(true).setAttribute("fundList", resultList);
			request.getSession(true).setAttribute("size", resultList.size());

			return "research-fund.jsp?page=1";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
