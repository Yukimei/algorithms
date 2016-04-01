package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class FundForm extends FormBean {
	private String name;
	private String symbol;

	public void setName(String s) {
		name = sanitize(s.trim());
	}

	public void setSymbol(String s) {
		symbol = sanitize(s.trim());
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if (name == null || name.length() == 0) {
			errors.add("You have to enter a name for the fund.");
		}

		if (name.length() > 10) {
			errors.add("Fund name need to be less than 10 characters");
		}

		if (symbol == null || symbol.length() == 0) {
			errors.add("You have to enter a ticker for the fund.");
		}

		if (symbol.length() > 5) {
			errors.add("The ticker needs to be shorter than 6 characters");
		}

		if (!isLetter(symbol)) {
			errors.add("Ticker must only contains letters.");
		}

		if (errors.size() > 0) {
			return errors;
		}

		return errors;
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}

	private boolean isLetter(String symbol) {
		int length = symbol.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isLetter(symbol.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}
