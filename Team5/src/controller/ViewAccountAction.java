package controller;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import databean.Customer;
import databean.PositionBean;
import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.PositionDAO;
import model.TransactionDAO;

public class ViewAccountAction extends Action {
	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;
	private PositionDAO positionDAO;

	private FundDAO fundDAO;

	public ViewAccountAction(Model model) {
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();

	}

	public String getName() {
		return "viewPortfolio";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		JSONObject error = new JSONObject();
		JSONArray array = new JSONArray();

		try {
			HttpSession session = request.getSession();
			boolean isCustomer = (boolean) session.getAttribute("isCustomer");
			Customer customer = (Customer) session.getAttribute("customer");
			// 由于不能使用request传参, 现在不使用username
			// String username = request.getParameter("username");

			// if is customer, there should be related information set in the
			// session.
			if (isCustomer) {
				if (customer == null) {
					// errors.add("No user is login");
					error.put("message", "You must log in prior to making this request");
					// return "login-customer.jsp";
					return error.toJSONString();
				}
			}

			if (customer == null) {
				String message = "I'm sorry you are not authorized to perform that action.";
				error.put("message", message);
				return error.toJSONString();
			}

			int customerId = customer.getCustomerId();
			customer = customerDAO.read(customerId);
			session.setAttribute("customer", customer);

			JSONObject result = new JSONObject();

			PositionBean[] beans = getPositionList(customer.getCustomerId());
			if (beans.length == 0) {
			    error.put("message", "You don't have any funds at this time");
			    return error.toJSONString();
			} else {
			for (int i = 0; i < beans.length; i++) {

				JSONObject success = new JSONObject();
				// success.put("Fund Name", viewBean[i].getfundName());
				// Change from getName to getSymbol
				success.put("symbol", fundDAO.read(beans[i].getFundId()).getSymbol());
				success.put("shares", beans[i].getShares() / 1000.00);
				success.put("price", (double) (fundDAO.read(beans[i].getFundId()).getInitial_value() / 100.00));
				array.add(i, success);
			}

			result.put("Funds: ", array);
			result.put("cash", customer.getAvailableCash());

			StringWriter out = new StringWriter();
			result.writeJSONString(out);

			return out.toString();
			}

		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "error.jsp";
		}
	}

	public PositionBean[] getPositionList(int customerId) throws RollbackException {
		PositionBean[] beans = positionDAO.getPositionList(customerId);
		return beans;
	}
}
