package formbean;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class PageForm extends FormBean {
	private String page;

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		page = sanitize(page.trim());
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if (page == null || page.length() == 0) {
			errors.add("The page number of the results is required.");
		}

		try {
			if (Integer.parseInt(page) <= 0) {
				errors.add("page number must be larger than 0");
				return errors;
			}
		} catch (NumberFormatException e) {
			errors.add("Page number is not an integer");
		}
	
		return errors;
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
