package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class CreateEmployeeForm extends FormBean {
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private String confirm;

	public String getFirstName() {
		return sanitize(firstName);
	}

	public String getLastName() {
		return sanitize(lastName);
	}

	public String getUserName() {
		return sanitize(userName);
	}

	public String getPassword() {
		return sanitize(password);
	}

	public String getConfirm() {
		return sanitize(confirm);
	}

	public void setFirstName(String s) {
		firstName = s.trim();
	}

	public void setLastName(String s) {
		lastName = s.trim();
	}

	public void setUserName(String s) {
		userName = s.trim();
	}

	public void setPassword(String s) {
		password = s.trim();
	}

	public void setConfirm(String s) {
		confirm = s.trim();
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (firstName == null || firstName.length() == 0) {
			errors.add("First Name is required.");
		} else if (firstName.matches(".*[<>\"].*")) {
			errors.add("First Name may not contain angle brackets or quotes");
		} else if (firstName.length() > 20) {
			errors.add("First name must be less than 20 letters");
		}

		if (lastName == null || lastName.length() == 0) {
			errors.add("Last Name is required.");
		} else if (lastName.matches(".*[<>\"].*")) {
			errors.add("Last Name may not contain angle brackets or quotes");
		} else if (lastName.length() > 20) {
			errors.add("Last name must be less than 20 letters");
		}

		if (userName == null || userName.length() == 0) {
			errors.add("Username is required.");
		} else if (userName.matches(".*[<>\"].*")) {
			errors.add("Username may not contain angle brackets or quotes");
		} else if (userName.length() > 20) {
			errors.add("Username must be less than 20 letters");
		}

		if (password == null || password.length() == 0) {
			errors.add("Password is required.");
		}

		if (password.length() > 20) {
			errors.add("Password must be less than 20 letters");
		}

		if (confirm == null || confirm.length() == 0) {
			errors.add("Confirm Password is required.");
		}

		if (!password.equals(confirm)) {
			errors.add("Incorrect passwords.");
		}

		if (errors.size() > 0) {
			return errors;
		}

		return errors;
	}

	private String sanitize(String s) {
		return s == null ? null : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
