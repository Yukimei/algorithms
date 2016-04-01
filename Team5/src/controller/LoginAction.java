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

import databean.Customer;
import databean.Employee;
import formbean.LoginForm;
import model.CustomerDAO;
import model.EmployeeDAO;
import model.Model;

public class LoginAction extends Action {
    private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory.getInstance(LoginForm.class);

    private EmployeeDAO employeeDAO;
    private CustomerDAO customerDAO;

    public LoginAction(Model model) {
        employeeDAO = model.getEmployeeDAO();
        customerDAO = model.getCustomerDAO();
    }

    public String getName() { return "login"; }

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
                error.put("Message", "form is not present");
            }

            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
//                return "login-employee.jsp";
                error.put("Message", "Validation error");
                return error.toJSONString();
            }
            

            Employee employee = employeeDAO.read(form.getUsername());
            Customer customer = customerDAO.read(form.getUsername());

            //这个部分要改
            if (employee == null && customer == null) {
                errors.add("We cannot find this account.");
//                return "login-employee.jsp";
                error.put("Message", "We cannot find this account");
                return error.toJSONString();
            }

            if (customer != null) {
                if (!customer.checkPwd(form.getPassword())) {
                    errors.add("Password is incorrect.");
                    //return "login-customer.jsp";
                    error.put("Message", "The username/password combination that you entered is not correct");
                    return error.toJSONString();
                }
            } else if (!employee.checkPwd(form.getPassword())) {
                errors.add("Password is incorrect.");
//              return "login-employee.jsp";
              error.put("Message", "The username/password combination that you entered is not correct");
              return error.toJSONString();
          }
            
//            if (error.size() >= 0) {
//                return error.toJSONString();
//            }

            HttpSession session = request.getSession();
            //这个部分要修改
            if (customer != null) {
                session.setAttribute("isCustomer", true);
            } else {
                session.setAttribute("isCustomer", false);
            }

            session.setAttribute("employee", employee);
            session.setAttribute("customer", customer);

            String username = request.getParameter("username");
            String message = "Welcome " + username;
            success.put("Message", message);
            if (customer != null) {
                JSONObject menu1 = new JSONObject();
                
                menu1.put("link", "/viewPortfolio");
                menu1.put("function", "View Portfolio");
                array.add(menu1);
                
                JSONObject menu2 = new JSONObject();
                menu2.put("link", "/buyFund");
                menu2.put("function", "Buy Fund");
                array.add(menu2);
                
                JSONObject menu3 = new JSONObject();
                menu3.put("function", "sellFund");
                menu3.put("link", "/Sell Fund");
                array.add(menu3);
                
                JSONObject menu4 = new JSONObject();
                menu4.put("link", "/requestCheck");
                menu4.put("function", "Request Check");
                array.add(menu4);
                
                JSONObject menu5 = new JSONObject();
                menu5.put("link", "/logout");
                menu5.put("function", "Logout");
                array.add(menu5);
                
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
            } else {
//            for (int i = 0; i < 5; i++) {
                JSONObject menu1 = new JSONObject();
                
                menu1.put("link", "/createCustomerAccount");
                menu1.put("function", "Create Customer Account");
                array.add(menu1);
                
                JSONObject menu2 = new JSONObject();
                menu2.put("link", "/depositCheck");
                menu2.put("function", "Deposit Check");
                array.add(menu2);
                
                JSONObject menu3 = new JSONObject();
                menu3.put("function", "Create Fund");
                menu3.put("link", "/createFund");
                array.add(menu3);
                
                JSONObject menu4 = new JSONObject();
                menu4.put("link", "/transitionDay");
                menu4.put("function", "Transition Day");
                array.add(menu4);
                
                JSONObject menu5 = new JSONObject();
                menu5.put("link", "/logout");
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
            }
            
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