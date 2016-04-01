package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class BuyFundForm extends FormBean {
	private String amount;
	private String fundId;

	public String getAmount() {
		return amount;
	}

	public String getFundId() {
		return fundId;
	}

	public void setAmount(String a) {
		amount = sanitize(a.trim());
	}

	public void setfundId(String s) {
		fundId = sanitize(s.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (amount == null || amount.trim().equals("")) {
			errors.add("An amount must be entered.");
			return errors;
		}

		try {
			Double.parseDouble(amount);
		} catch (NumberFormatException e) {
			errors.add("Sorry, purchase amount must be a number.");
			return errors;
		}

		if (fundId == null || fundId.trim().equals("")) {
			errors.add("Please enter a valid Fund ID.");
		}

		try {
			Integer.parseInt(fundId);
		} catch (NumberFormatException e) {
			errors.add("Sorry, fund ID must be a valid number.");
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