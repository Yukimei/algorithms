package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class DepositCheckForm extends FormBean {
	private String amount;

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = sanitize(amount.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (amount == null || amount.length() == 0) {
			errors.add("Please enter an amount.");
			return errors;
		}

		try {
			Double.parseDouble(amount);
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
