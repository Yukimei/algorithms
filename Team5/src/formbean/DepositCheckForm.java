package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class DepositCheckForm extends FormBean {
	private String cash;
	private String username;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = sanitize(username.trim());
	}

	public String getCash() {
		return cash;
	}

	public void setCash(String cash) {
		this.cash = sanitize(cash.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (username == null || username.length() == 0) {
			errors.add("Please enter a username.");
			return errors;
		}

		if (cash == null || cash.length() == 0) {
			errors.add("Please enter an amount.");
			return errors;
		}

		try {
			Double.parseDouble(cash);
		} catch (NumberFormatException e) {
			errors.add("Amount must be digital numbers.");
			return errors;
		}

		if (errors.size() > 0) {
			return errors;
		}
		return errors;
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}

}
