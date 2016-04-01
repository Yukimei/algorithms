package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class SellFundForm extends FormBean {
	private String shares;
	private String fundId;

	public String getShares() {
		return shares;
	}

	public String getFundId() {
		return fundId;
	}

	public void setShares(String s) {
		shares = sanitize(s.trim());
	}

	public void setfundId(String s) {
		fundId = sanitize(s.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (shares == null || shares.trim().equals("")) {
			errors.add("Please enter a valid shares amount.");
		}
		try {
			if (Double.parseDouble(shares) <= 0) {
				errors.add("Number of shares must be larger than 0");
				return errors;
			}
		} catch (NumberFormatException e) {
			errors.add("Shares amount must be a valid number.");
			return errors;
		}

		if (fundId == null || fundId.trim().equals("")) {
			errors.add("Please enter a valid Fund ID.");
			return errors;
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
