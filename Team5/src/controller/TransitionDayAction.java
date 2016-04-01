package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.TransactionDAO;

public class TransitionDayAction extends Action {

	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;

	private FundDAO fundDAO;
	private Model model;
	private Lock lock = new ReentrantLock();

	public TransitionDayAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();

		fundDAO = model.getFundDAO();
		this.model = model;
	}

	public String getName() {
		return "transitionDay";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		JSONObject error = new JSONObject();
		JSONObject success = new JSONObject();

		// try {
		HttpSession session = request.getSession();
		Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
		if (isCustomer) {
			// errors.add("Sorry, Only employees are eligible to access this
			// page.");
			// return "view-account.jsp";
			String message = "I’m sorry you are not authorized to perform that action";
			error.put("message", message);
			return error.toJSONString();
		}

		System.out.println("update fund prices");

		int flag;
		lock.lock();
		try {
			flag = fundDAO.updateFundPriceHelper();
		} finally {
			lock.unlock();
		}

		if (flag == 1) {
			// success
			System.out.println("finished transaction for deposit/request check");

			request.setAttribute("message", "The fund prices have been recalculated");
			// session.setAttribute("fundList", model.getFundList());

			success.put("messege", "The fund prices have been recalculated");
			StringWriter out = new StringWriter();
			try {
				success.writeJSONString(out);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return out.toString();

		} else {
			// price fluctuate more than 10%
			error.put("messege", "sorry, some errors happened, please set price again. ");
			StringWriter errorOut = new StringWriter();

			try {
				error.writeJSONString(errorOut);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return errorOut.toString();

			// return errorOut.toString();
		}
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}