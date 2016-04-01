package controller;

import java.util.ArrayList;
import java.util.List;

//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

//import databeans.FavoriteBean;
import formbean.CreateEmployeeForm;
//import formbean.RegisterForm;
import model.EmployeeDAO;
//import model.FavoriteDAO;
import model.Model;

public class CreateEmployeeAction extends Action {
    private FormBeanFactory<CreateEmployeeForm> formBeanFactory = FormBeanFactory.getInstance(CreateEmployeeForm.class);

    private EmployeeDAO employeeDAO;

    public CreateEmployeeAction(Model model) {
        employeeDAO = model.getEmployeeDAO();
    }


	public String getName() {
		return "create_employee.do";
	}

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors", errors);
        
        HttpSession session = request.getSession();

        boolean isCustomer = (boolean) session.getAttribute("isCustomer");
        if (isCustomer) {
            errors.add("Only employees can create accounts");
            return "view-account.jsp";
        }

        try {
            CreateEmployeeForm form = formBeanFactory.create(request);
            request.setAttribute("form", form);

            
            if (!form.isPresent()) {
                return "create-employee.jsp";
            }
            // 判断是否已经存在
            if (employeeDAO.read(form.getUserName()) != null) {
                errors.add("This username " + form.getUserName() + " has already been registered");
                return "create-employee.jsp";
            }

            // Any validation errors?
            errors.addAll(form.getValidationErrors());
            if (errors.size() != 0) {
                return "create-employee.jsp";
            }
            
            int flag = employeeDAO.createEmployee(form.getUserName(), 
                    form.getFirstName(), form.getLastName(), form.getPassword());

            
            if (flag == -1) {
                errors.add("This username " + form.getUserName() + " has already been registered");
                return "create-employee.jsp";
            }
            
            if (flag == 1) {
                request.setAttribute("emessage", "Employee " + form.getUserName() + " has been created");
                request.removeAttribute("form");
            }
            
            return "create-employee.jsp";

        } catch (RollbackException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        } catch (FormBeanException e) {
            errors.add(e.getMessage());
            return "error.jsp";
        }
    }
}