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
import formbean.LoginForm;
import model.CustomerDAO;
import model.Model;

public class LoginActionCustomer extends Action {
    private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory.getInstance(LoginForm.class);

    private CustomerDAO customerDAO;

    public LoginActionCustomer(Model model) {
        customerDAO = model.getCustomerDAO();
    }

    public String getName() { return "login_customer"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        
        JSONObject error = new JSONObject();
        JSONObject success = new JSONObject();
        
        try {
        	LoginForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            if (!form.isPresent()) {
                return "login-customer.jsp";
            }

            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                //return "login-customer.jsp";
            	error.put("Message", "Validation error");
                return error.toJSONString();
            }

            Customer customer = customerDAO.read(form.getUsername());

            if (customer == null) {
                errors.add("We cannot find this customer.");
                //return "login-customer.jsp";
                error.put("Message", "We cannot find this customer");
                return error.toJSONString();
            }

            if (!customer.checkPwd(form.getPassword())) {
                errors.add("Password is incorrect.");
                //return "login-customer.jsp";
                error.put("Message", "Password is incorrect");
                error.toJSONString();
            }

            HttpSession session = request.getSession();
            session.setAttribute("isCustomer", true);
            session.setAttribute("customer", customer);

            String username = request.getParameter("username");
            String message = "Welcome " + username;
            success.put("message", message);
            return success.toJSONString();
           // return "view_account.do";
        } catch (RollbackException e) {
            errors.add(e.getMessage());
            //return "error.jsp";
            return error.toJSONString();
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            //return "error.jsp";
            return error.toJSONString();
        }
    }
}
