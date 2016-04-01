package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class CreateCustomerForm extends FormBean {
	private String userName;
	private String password;
	private String confirm;
	private String firstName;
	private String lastName;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String state;
	private String zip;

	public String getUserName() {
		return sanitize(userName);
	}

	public String getPassword() {
		return sanitize(password);
	}

	public String getConfirm() {
		return sanitize(confirm);
	}

	public String getFirstName() {
		return sanitize(firstName);
	}

	public String getLastName() {
		return sanitize(lastName);
	}

	public String getAddrLine1() {
		return sanitize(addrLine1);
	}

	public String getAddrLine2() {
		return sanitize(addrLine2);
	}

	public String getCity() {
		return sanitize(city);
	}

	public String getState() {
		return sanitize(state);
	}

	public String getZip() {
		return sanitize(zip);
	}

	public void setUserName(String s) {
		userName = s.trim();
	}

	public void setFirstName(String s) {
		firstName =s.trim();
	}

	public void setLastName(String s) {
		lastName = s.trim();
	}

	public void setPassword(String s) {
		password = s.trim();
	}

	public void setConfirm(String s) {
		confirm = s.trim();
	}

	public void setAddrLine1(String s) {
		addrLine1 = s.trim();
	}

	public void setAddrLine2(String s) {
		addrLine2 = s.trim();
	}

	public void setCity(String s) {
		city = s.trim();
	}

	public void setState(String s) {
		state = s.trim();
	}

	public void setZip(String s) {
		zip = s.trim();
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();

		if (firstName == null || firstName.length() == 0) {
			errors.add("First Name is required.");
		} else if (firstName.matches(".*[<>\"].*")) {
			errors.add("First Name may not contain angle brackets or quotes");
		} else if (firstName.length() > 20) {
			errors.add("First name must be less than 20 letters");
		}

		if (lastName == null || lastName.length() == 0) {
			errors.add("Last Name is required.");
		} else if (lastName.matches(".*[<>\"].*")) {
			errors.add("Last Name may not contain angle brackets or quotes");
		} else if (lastName.length() > 20) {
			errors.add("Last name must be less than 20 letters");
		}

		if (userName == null || userName.length() == 0) {
			errors.add("Username is required.");
		} else if (userName.matches(".*[<>\"].*")) {
			errors.add("Username may not contain angle brackets or quotes");
		} else if (userName.length() > 10) {
			errors.add("Username must be less than 10 letters");
		}

		if (password == null || password.length() == 0) {
			errors.add("Password is required.");
		}

		if (password.length() > 20) {
			errors.add("Password must be less than 20 letters");
		}

		if (confirm == null || confirm.length() == 0) {
			errors.add("Confirm Password is required.");
		}

		if (addrLine1 == null || addrLine1.length() == 0) {
			errors.add("Address Line1 is required.");
		} else if (addrLine1.matches(".*[<>\"].*")) {
			errors.add("Address Line1 may not contain angle brackets or quotes");
		} else if (addrLine1.length() > 15) {
			errors.add("Address Line1 must be less than 15 letters");
		}

		if (addrLine2 == null || addrLine2.length() == 0) {
			errors.add("Address Line2 is required.");
		} else if (addrLine2.matches(".*[<>\"].*")) {
			errors.add("address Line2 may not contain angle brackets or quotes");
		} else if (addrLine2.length() > 15) {
			errors.add("Address Line2 must be less than 15 letters");
		}

		if (city == null || city.length() == 0) {
			errors.add("City is required.");
		} else if (city.matches(".*[<>\"].*")) {
			errors.add("City may not contain angle brackets or quotes");
		} else if (city.length() > 20) {
			errors.add("City must be less than 20 letters");
		}

		if (state == null || state.length() == 0) {
			errors.add("State is required.");
		} else if (state.matches(".*[<>\"].*")) {
			errors.add("State may not contain angle brackets or quotes");
		} else if (state.length() > 15) {
			errors.add("State must be less than 15 letters");
		}

		if (zip == null || zip.length() == 0) {
			errors.add("Zipcode is required.");
		}

		if (zip.length() != 5) {
			errors.add("Zipcode must be a 5-digit number.");
		}

		try {
		} catch (NumberFormatException e) {
			errors.add("Zipcode must be digital numbers.");
		}

		if (!password.equals(confirm)) {
			errors.add("Incorrect Password.");
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
