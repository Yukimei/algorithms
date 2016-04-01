package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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

    public String getName() { return "login_employee"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);

        JSONObject error = new JSONObject();
        JSONObject success = new JSONObject();
        JSONArray array = new JSONArray();
//        JSONObject menu = new JSONObject();
        JSONObject result = new JSONObject();
//        success.put("message", "Welcome");
//        return success.toJSONString();
        try {
            LoginForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            if (!form.isPresent()) {
               return "login-employee.jsp";
            }

            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
//                return "login-employee.jsp";
                error.put("Message", "Validation error");
                return error.toJSONString();
            }
            
            //Look up employee
            Employee employee = employeeDAO.read(form.getUsername());

            if (employee == null) {
                errors.add("We cannot find this employee.");
//                return "login-employee.jsp";
                error.put("Message", "We cannot find this employee");
                return error.toJSONString();
            }
            
            //Check the password
            if (!employee.checkPwd(form.getPassword())) {
                errors.add("Password is incorrect.");
//                return "login-employee.jsp";
                error.put("Message", "The username/password combination that you entered is not correct");
                error.toJSONString();
            }
            
//            if (error.size() >= 0) {
//                return error.toJSONString();
//            }

            HttpSession session = request.getSession();
            session.setAttribute("isCustomer", false);
            session.setAttribute("employee", employee);

            String username = request.getParameter("username");
            String message = "Welcome " + username;
            success.put("Message", message);
//            for (int i = 0; i < 5; i++) {
                JSONObject menu1 = new JSONObject();
                
                menu1.put("link", "/create_customer.do");
                menu1.put("function", "Create Account");
                array.add(menu1);
                
                JSONObject menu2 = new JSONObject();
                menu2.put("link", "/deposit_check.do");
                menu2.put("function", "Deposit Check");
                array.add(menu2);
                
                JSONObject menu3 = new JSONObject();
                menu3.put("function", "Create Fund");
                menu3.put("link", "/create_fund.do");
                array.add(menu3);
                
                JSONObject menu4 = new JSONObject();
                menu4.put("link", "/transition_day.do");
                menu4.put("function", "Transition Day");
                array.add(menu4);
                
                JSONObject menu5 = new JSONObject();
                menu5.put("link", "/logout.do");
                menu5.put("function", "Logout");
                array.add(menu5);
//            }
            
                result.put("Menu", array);
            
            StringWriter out = new StringWriter();
            try {
                success.writeJSONString(out);
                result.writeJSONString(out);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return out.toString();
//            return "customer_list.do";
            
        } catch (RollbackException e) {
            errors.add(e.getMessage());
//            return "error.jsp";
            return error.toJSONString();
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
//            return "error.jsp";
            return error.toJSONString();
        }
    }
}
