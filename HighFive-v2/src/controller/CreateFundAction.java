package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import formbean.FundForm;
import model.FundDAO;
import model.Model;

public class CreateFundAction extends Action {

	private FormBeanFactory<FundForm> fundFormFactory = FormBeanFactory.getInstance(FundForm.class);
	private FundDAO fundDAO;

	public CreateFundAction(Model model) {
		fundDAO = model.getFundDAO();
	}

	public String getName() {
		return "create_fund.do";
	}

	public String perform(HttpServletRequest request) {

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		HttpSession session = request.getSession();

		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			errors.add("You must be a employee to create a fund.");
			return "view-account.jsp";
		}

		try {
			FundForm fundForm = fundFormFactory.create(request);
			request.setAttribute("form", fundForm);

			if (!fundForm.isPresent()) {
				return "create_fund.jsp";
			}

			errors.addAll(fundForm.getValidationErrors());
			if (errors.size() > 0) {
				return "create_fund.jsp";
			}

			if (fundDAO.nameExist(fundForm.getName())) {
				errors.add("There is already a fund who has the same name.");
				return "create_fund.jsp";
			}

			if (fundDAO.symbolExist(fundForm.getSymbol())) {
				errors.add("There is already a fund who has the same ticker.");
				return "create_fund.jsp";
			}
			
			int flag = fundDAO.createFundHelper(fundForm.getName(), fundForm.getSymbol());
			if (flag == -1) {
				errors.add("There is already a fund who has the same name.");
				return "createFund.jsp";
			}
			
			if (flag == -2) {
				errors.add("There is already a fund who has the same ticker.");
				return "createFund.jsp";
			}
			
			if (flag == 1) {
				request.setAttribute("message", "New fund " + fundForm.getName() + " is created.");
			}

			return "create_fund.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
