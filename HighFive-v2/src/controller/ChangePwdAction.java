package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.Employee;
import formbean.ChangePwdForm;
import model.CustomerDAO;
import model.EmployeeDAO;
import model.Model;

public class ChangePwdAction extends Action {
	private FormBeanFactory<ChangePwdForm> formBeanFactory = FormBeanFactory.getInstance(ChangePwdForm.class);

	private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;

	public ChangePwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
		employeeDAO = model.getEmployeeDAO();
	}

	public String getName() {
		return "change_pwd.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {

			HttpSession session = request.getSession();
			Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");

			// Load the form parameters into a form bean
			ChangePwdForm form = formBeanFactory.create(request);

			if (!form.isPresent()) {
				if (session.getAttribute("password") == null) {
					if (isCustomer) {
						// if(c != null) {
						Customer customer = (Customer) session.getAttribute("customer");
						String originalCP = customer.getHashedPassword();
						session.setAttribute("password", originalCP);
						// }
					} else {
						Employee employee = (Employee) session.getAttribute("employee");
						String originalEP = employee.getHashedPassword();
						session.setAttribute("password", originalEP);
					}
				}
				return "change-pwd.jsp";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "change-pwd.jsp";
			}

			// change password
			if (isCustomer) {
				// if(c != null) {
				String originalPW = (String) session.getAttribute("password");
				Customer customer = (Customer) session.getAttribute("customer");
				Customer customer2 = customerDAO.read(customer.getUserName());
				String newPW = customer2.getHashedPassword();
				if (originalPW.equals(newPW)) {
					customerDAO.setPwd(customer.getUserName(), form.getNewPassword());
					request.setAttribute("message", "Password has been changed.");
					Customer customer3 = customerDAO.read(customer2.getUserName());
					session.setAttribute("password", customer3.getHashedPassword());
				} else {
					errors.add("Bazinga! Your ex-girlfriend has already changed your password.");
				}
				// }
			} else {
				String originalPW = (String) session.getAttribute("password");
				Employee employee = (Employee) session.getAttribute("employee");
				Employee employee2 = employeeDAO.read(employee.getUserName());
				String newPW = employee2.getHashedPassword();
				if (originalPW.equals(newPW)) {
					employeeDAO.setPassword(employee.getUserName(), form.getNewPassword());
					request.setAttribute("message", "Password has been changed.");
					Employee employee3 = employeeDAO.read(employee2.getUserName());
					session.setAttribute("password", employee3.getHashedPassword());
				} else {
					errors.add("Bazinga! Your ex-girlfriend has already changed your password.");
				}
			}

			return "change-pwd.jsp";

		} catch (RollbackException e) {
			errors.add("Concurrent user");
			return "change-pwd.jsp";
		} catch (FormBeanException e) {
			errors.add("Input format error");
			return "change-pwd.jsp";
		}
	}
}
