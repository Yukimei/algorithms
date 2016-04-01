package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class CreateCustomerForm extends FormBean {
	private String username;
	private String password;
	private String confirm;
	private String firstname;
	private String lastname;
	private String addr_line1;
	private String addr_line2;
	private String city;
	private String state;
	private String zip;

	public String getUsername() {
		return sanitize(username);
	}

	public String getPassword() {
		return sanitize(password);
	}

	public String getConfirm() {
		return sanitize(confirm);
	}

	public String getFirstname() {
		return sanitize(firstname);
	}

	public String getLastname() {
		return sanitize(lastname);
	}

	public String getAddr_line1() {
		return sanitize(addr_line1);
	}

	public String getAddr_line2() {
		return sanitize(addr_line2);
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

	public void setUsername(String s) {
		username = s.trim();
	}

	public void setFirstname(String s) {
		firstname = s.trim();
	}

	public void setLastname(String s) {
		lastname = s.trim();
	}

	public void setPassword(String s) {
		password = s.trim();
	}

	public void setConfirm(String s) {
		confirm = s.trim();
	}

	public void setAddr_line1(String s) {
		addr_line1 = s.trim();
	}

	public void setAddr_line2(String s) {
		addr_line2 = s.trim();
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

		if (firstname == null || firstname.length() == 0) {
			errors.add("First Name is required.");
		} else if (firstname.matches(".*[<>\"].*")) {
			errors.add("First Name may not contain angle brackets or quotes");
		} else if (firstname.length() > 20) {
			errors.add("First name must be less than 20 letters");
		}

		if (lastname == null || lastname.length() == 0) {
			errors.add("Last Name is required.");
		} else if (lastname.matches(".*[<>\"].*")) {
			errors.add("Last Name may not contain angle brackets or quotes");
		} else if (lastname.length() > 20) {
			errors.add("Last name must be less than 20 letters");
		}

		if (username == null || username.length() == 0) {
			errors.add("Username is required.");
		} else if (username.matches(".*[<>\"].*")) {
			errors.add("Username may not contain angle brackets or quotes");
		} else if (username.length() > 10) {
			errors.add("Username must be less than 10 letters");
		}

		if (password == null || password.length() == 0) {
			errors.add("Password is required.");
		}

		if (password.length() > 20) {
			errors.add("Password must be less than 20 letters");
		}

		// if (confirm == null || confirm.length() == 0) {
		// errors.add("Confirm Password is required.");
		// }

		if (addr_line1 == null || addr_line1.length() == 0) {
			errors.add("Address Line1 is required.");
		} else if (addr_line1.matches(".*[<>\"].*")) {
			errors.add("Address Line1 may not contain angle brackets or quotes");
		}
		// else if (addrLine1.length() > 30) {
		// errors.add("Address Line1 must be less than 30 letters");
		// }

		if (addr_line2 == null || addr_line2.length() == 0) {
			errors.add("Address Line2 is required.");
		} else if (addr_line2.matches(".*[<>\"].*")) {
			errors.add("address Line2 may not contain angle brackets or quotes");
		}
		// else if (addrLine2.length() > 30) {
		// errors.add("Address Line2 must be less than 30 letters");
		// }

		if (city == null || city.length() == 0) {
			errors.add("City is required.");
		} else if (city.matches(".*[<>\"].*")) {
			errors.add("City may not contain angle brackets or quotes");
		} else if (city.length() > 20) {
			errors.add("City must be less than 20 letters");
		}

		if (state == null || state.length() == 0) {
			errors.add("State is required.");
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
		if (errors.size() > 0) {
			return errors;

		}
		return errors;
	}

	private String sanitize(String s) {
		return s == null ? null
				: s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
