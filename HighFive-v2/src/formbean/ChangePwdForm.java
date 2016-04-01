package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class ChangePwdForm extends FormBean {
	private String confirmPwd;
	private String newPwd;

	public void setConfirmPassword(String s) {
		confirmPwd = s.trim();
	}

	public void setNewPassword(String s) {
		newPwd = s.trim();
	}

	public String getConfirmPassword() {
		return sanitize(confirmPwd);
	}

	public String getNewPassword() {
		return sanitize(newPwd);
	}

	public List<String> getValidationErrors() {
		
		List<String> errors = new ArrayList<String>();
		if (newPwd == null || newPwd.length() == 0)
			errors.add("Please input a password.");
		if (confirmPwd == null || confirmPwd.length() == 0)
			errors.add("Please confirm your password.");
		if (errors.size() > 0)
			return errors;
		if (!newPwd.equals(confirmPwd))
			errors.add("The two passwords have to be identical.");
		return errors;
	}

	private String sanitize(String s) {
		return s == null ? null : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}

}
