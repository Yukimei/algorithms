package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class LoginForm extends FormBean {
	private String username;
	private String password;

	public String getUsername() {
		return sanitize(username);
	}

	public String getPassword() {
		return sanitize(password);
	}

	public void setPassword(String s) {
		password = s.trim();
	}
	
	public void setUsername(String s) {
		username = s.trim();
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if (username == null || username.length() == 0) {
			errors.add("Please enter an username.");
		}
		if (password == null || password.length() == 0) {
			errors.add("Please enter a password.");
		}
		
		if (username.matches(".*[<>\"].*")) {
			errors.add("User Name may not contain angle brackets or quotes");
		}
		
		if (password.matches(".*[<>\"].*")) {
			errors.add("Password may not contain angle brackets or quotes");
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
