package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class RequestCheckForm extends FormBean {
	private String amount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String s) {
		amount = sanitize(s);
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (amount == null || amount.length() == 0) {
			errors.add("Please enter an amount.");
			return errors;
		}

		try {
			Double.parseDouble(amount);
			if (Double.parseDouble(amount) <= 0.01) {
				errors.add("Amount must be larger than $0.01");
				return errors;
			}
		} catch (NumberFormatException e) {
			errors.add("Amount needs to be numbers.");
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
