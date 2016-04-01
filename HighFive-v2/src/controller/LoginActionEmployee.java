package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Employee;
import formbean.LoginForm;
import model.EmployeeDAO;
import model.Model;

public class LoginActionEmployee extends Action {
    private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory.getInstance(LoginForm.class);

    private EmployeeDAO employeeDAO;

    public LoginActionEmployee(Model model) {
        employeeDAO = model.getEmployeeDAO();
    }

    public String getName() { return "login_employee.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        try {
            LoginForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            if (!form.isPresent()) {
                return "login-employee.jsp";
            }

            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "login-employee.jsp";
            }
            
            //Look up employee
            Employee employee = employeeDAO.read(form.getUsername());

            if (employee == null) {
                errors.add("We cannot find this employee.");
                return "login-employee.jsp";
            }
            
            //Check the password
            if (!employee.checkPwd(form.getPassword())) {
                errors.add("Password is incorrect.");
                return "login-employee.jsp";
            }

            HttpSession session = request.getSession();
            session.setAttribute("isCustomer", false);
            session.setAttribute("employee", employee);


            return "customer_list.do";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}
