package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class SearchCustomerForm extends FormBean {
	private String search;

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = sanitize(search.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		
		if (search == null || search.length() == 0) {
			errors.add("The username of a customer is required.");
		}
		
		if (search.matches(".*[<>\"].*")) {
			errors.add("Search key words may not contain angle brackets or quotes");
		}
		return errors;
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
