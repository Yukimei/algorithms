package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import databean.Customer;
import databean.Employee;
import model.Model;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {
		Model model = null;
		try {
			model = new Model(getServletConfig());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Action.add(new TransactionHistoryAction(model));
		Action.add(new CreateFundAction(model));
		Action.add(new TransitionDayAction(model));
		Action.add(new CreateCustomerAction(model));
		Action.add(new CreateEmployeeAction(model));
		Action.add(new ViewCustomerAction(model));
		Action.add(new ViewAccountAction(model));
		Action.add(new ChangePwdAction(model));
		Action.add(new ResetPwdAction(model));
		Action.add(new BuyFundAction(model));
		Action.add(new ResearchFundAction(model));
		Action.add(new FundStatsAction(model));
		Action.add(new DepositCheckAction(model));
		Action.add(new RequestCheckAction(model));
		Action.add(new SellFundAction(model));
		Action.add(new FundListTurnPageAction(model));
		Action.add(new CustomerListTurnPageAction(model));
		Action.add(new ViewTransactionTurnPageAction(model));
		Action.add(new LoginActionEmployee(model));
		Action.add(new LoginActionCustomer(model));
		Action.add(new LogoutAction());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String nextPage = performAction(request);
		sendToNextPage(nextPage, request, response);
	}

	private String performAction(HttpServletRequest request) {

		HttpSession session = request.getSession(true);
		String servletPath = request.getServletPath();
		String action = getActionName(servletPath);

		Customer customer = (Customer) session.getAttribute("customer");
		Employee employee = (Employee) session.getAttribute("employee");

		if (action.equals("login_employee.do") || action.equals("login_customer.do")) {
			return Action.perform(action, request);
		}

		if (customer == null && employee == null) {
			return "index.jsp";
		}
		return Action.perform(action, request);
	}

	private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		if (nextPage == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getServletPath());
			return;
		}

		if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.endsWith(".jsp")) {
			RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + nextPage);
			d.forward(request, response);
			return;
		}

		if (nextPage.startsWith("http") || nextPage.startsWith("ftp") || nextPage.startsWith("file")) {
			response.sendRedirect(nextPage);
			return;
		}

		if (nextPage.contains(".jsp?page=")) {
			RequestDispatcher d = request.getRequestDispatcher("WEB-INF/" + nextPage);
			d.forward(request, response);
			return;
		}

		throw new ServletException(Controller.class.getName() + ".sendToNextPage(\"" + nextPage + "\"): invalid url.");

	}

	private String getActionName(String path) {

		int slash = path.lastIndexOf('/');
		return path.substring(slash + 1);
	}
}
