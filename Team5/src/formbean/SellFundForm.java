package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class SellFundForm extends FormBean {
	private String numShares;
	private String fundSymbol;

	public String getNumShares() {
		return numShares;
	}

	public void setNumShares(String numShares) {
		this.numShares = sanitize(numShares);
	}

	public String getFundSymbol() {
		return fundSymbol;
	}

	public void setFundSymbol(String fundSymbol) {
		this.fundSymbol = sanitize(fundSymbol);
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (numShares == null || numShares.trim().equals("")) {
			errors.add("An numShares must be entered.");
			return errors;
		}

		if (fundSymbol == null || fundSymbol.length() == 0) {
			errors.add("You have to enter a ticker for the fund.");
		}

		try {
			Integer.parseInt(numShares);
		} catch (NumberFormatException e) {
			errors.add("Sorry, purchase numShares must be a number.");
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
