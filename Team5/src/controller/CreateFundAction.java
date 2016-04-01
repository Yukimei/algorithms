package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONObject;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.Employee;
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
		return "createFund";
	}

	public String perform(HttpServletRequest request) {

		JSONObject obj = new JSONObject();

		List<String> errors = new ArrayList<String>();
		HttpSession session = request.getSession();

		Customer customer = (Customer) session.getAttribute("customer");
		Employee employee = (Employee) session.getAttribute("employee");

		if (customer == null && employee == null) {
			String message = "You must log in prior to make this request";
			obj.put("message", message);
			return obj.toJSONString();
		}
		
		

		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			String message = "I'm sorry you are not authorized to preform that action";
			obj.put("message", message);
			return obj.toJSONString();
		}

		try {
			FundForm fundForm = fundFormFactory.create(request);
			request.setAttribute("form", fundForm);

			if (!fundForm.isPresent()) {
				String message = "I'm sorry, a there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			errors.addAll(fundForm.getValidationErrors());
			if (errors.size() > 0) {
				String message = "I’m sorry, b there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			if (fundDAO.nameExist(fundForm.getName())) {
				String message = "I’m sorry, c there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			if (fundDAO.symbolExist(fundForm.getSymbol())) {
				String message = "I’m sorry, d there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			double value = Double.parseDouble(fundForm.getInitial_value());

			int flag = fundDAO.createFundHelper(fundForm.getName(), fundForm.getSymbol(), value);
			if (flag == -1) {
				String message = "I’m sorry, there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			if (flag == -2) {
				String message = "I’m sorry, there was a problem creating the fund";
				obj.put("message", message);
				return obj.toJSONString();
			}

			if (flag == 1) {
				String message = "The fund has been successfully created";
				obj.put("message", message);
				return obj.toJSONString();
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
