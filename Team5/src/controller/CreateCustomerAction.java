package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONObject;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;


import formbean.CreateCustomerForm;
import model.CustomerDAO;
import model.Model;

public class CreateCustomerAction extends Action {
	private FormBeanFactory<CreateCustomerForm> formBeanFactory = FormBeanFactory.getInstance(CreateCustomerForm.class);

	private CustomerDAO customerDAO;

	public CreateCustomerAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "createCustomerAccount";
	}

	public String perform(HttpServletRequest request) {
		
		JSONObject obj = new JSONObject();

		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		HttpSession session = request.getSession();

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			String message = "I'm sorry you are not authorized to preform that action";
			obj.put("message", message);
			return obj.toJSONString();
		}

		try {

			CreateCustomerForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			if (!form.isPresent()) {
				String message = "You must log in prior to making this request";
				obj.put("message", message);
                for (String a : errors) {
                    System.out.println(a);
                }
				return obj.toJSONString();
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				String message = "“I'm sorry, there was a problem creating the account";
				obj.put("message", message);
				System.out.println("FirstName = " + form.getFirstname());
				for (String a : errors) {
				    System.out.println(a);
				}
				return obj.toJSONString();
			}

			if (customerDAO.read(form.getUsername()) != null) {
//			    System.out.println(form.getUsername());
				String message = ("I'm sorry, there was a problem creating the account");
				obj.put("message", message);
                for (String a : errors) {
                    System.out.println(a);
                }
				return obj.toJSONString();

			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				String message = ("I'm sorry, there was a problem creating the account");
				obj.put("message", message);
                for (String a : errors) {
                    System.out.println(a);
                }
				return obj.toJSONString();
			}


			int flag = customerDAO.createCustomer(form.getUsername(), form.getFirstname(), form.getLastname(),
					form.getPassword(), form.getAddr_line1(), form.getAddr_line2(), form.getCity(), form.getState(),
					form.getZip());

			if (flag == -1) {
				String message = ("I'm sorry, there was a problem creating the account");
				obj.put("message", message);
                for (String a : errors) {
                    System.out.println(a);
                }
				return obj.toJSONString();
			}

			if (flag == 1) {

				String message = ("The account has been successfully created");
				obj.put("message", message);
				return obj.toJSONString();
			}


			String message = ("I'm sorry, e there was a problem creating the account");
			obj.put("message", message);
            for (String a : errors) {
                System.out.println(a);
            }
			return obj.toJSONString();

		} catch (RollbackException e) {
			String message = ("I'm sorry, f there was a problem creating the account");
			obj.put("message", message);
            for (String a : errors) {
                System.out.println(a);
            }
			return obj.toJSONString();
		} catch (FormBeanException e) {
			String message = ("I'm sorry, g there was a problem creating the account");
			obj.put("message", message);
            for (String a : errors) {
                System.out.println(a);
            }
			return obj.toJSONString();
		}
	}
}
