package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class BuyFundForm extends FormBean {
	private String cashValue;
	private String fundSymbol;

	public String getcashValue() {
		return cashValue;
	}

	public String getfundSymbol() {
		return fundSymbol;
	}

	public void setcashValue(String a) {
		cashValue= sanitize(a.trim());
	}

	public void setfundSymbol(String s) {
		fundSymbol = sanitize(s.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (cashValue== null || cashValue.trim().equals("")) {
			errors.add("An cashValue must be entered.");
			return errors;
		}

		if (fundSymbol == null || fundSymbol.length() == 0) {
			errors.add("You have to enter a ticker for the fund.");
		}

		if (fundSymbol.length() > 5) {
			errors.add("The ticker needs to be shorter than 6 characters");
		}
		
		if (!isLetter(fundSymbol)) {
			errors.add("Ticker must only contains letters.");
		}
		
		try {
			Double.parseDouble(cashValue);
		} catch (NumberFormatException e) {
			errors.add("Sorry, purchase cashValue must be a number.");
			return errors;
		}
		
		if (errors.size() > 0) {
		    for(String haha: errors) {
		        System.out.println("Buy Form" + haha);
		    }
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