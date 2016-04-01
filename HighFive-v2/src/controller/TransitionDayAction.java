package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;

import databean.Customer;
import databean.DateBean;
import databean.PriceHistoryBean;
import databean.TransactionBean;
import databean.TransitionDayBean;
import model.CustomerDAO;
import model.DateDAO;
import model.Model;
import model.PositionDAO;
import model.PriceHistoryDAO;
import model.TransactionDAO;

public class TransitionDayAction extends Action {
	private SimpleDateFormat dateFormat;
	private PriceHistoryDAO priceHistoryDAO;
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;
	private PositionDAO positionDAO;
	private DateDAO dateDAO;
	private Model model;

	public TransitionDayAction(Model model) {
		dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		dateFormat.setLenient(false);
		priceHistoryDAO = model.getPriceHistoryDAO();
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();
		dateDAO = model.getDateDAO();
		this.model = model;
	}

	public String getName() {
		return "transition_day.do";
	}

	@Override
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			HttpSession session = request.getSession();
			Boolean isCustomer = (Boolean) session.getAttribute("isCustomer");
			if (isCustomer) {
				errors.add("Sorry, Only employees are eligible to access this page.");
				return "view-account.jsp";
			}
			String action = request.getParameter("makeTransition");

			if (action == null || !action.equals("New Transition Begins")) {
				session.setAttribute("fundList", model.getFundList());
				session.setAttribute("fundListLength", model.getFundList().length);
				if (dateDAO.getLastDate() != null)
					request.setAttribute("date", dateFormat.format(dateDAO.getLastDate()));
				else
					request.setAttribute("date", null);
				return "transition-day.jsp";
			}
			// deal with concurrency (create fund and transition day in the same
			// time)
			TransitionDayBean[] firstList = model.getFundList();
			Integer a = (Integer) session.getAttribute("fundListLength");
			if (a != null && firstList.length != 0) {
				if (firstList.length != a) {
					errors.add("Another user has just created a fund, please try again.");
					session.setAttribute("fundList", model.getFundList());
					session.setAttribute("fundListLength", model.getFundList().length);
					return "transition-day.jsp";
				}
			}

			session.setAttribute("fundList", model.getFundList());
			session.setAttribute("fundListLength", model.getFundList().length);

			String inputDate = request.getParameter("date");
			String[] fundIds = request.getParameterValues("fundIds");
			String[] prices = request.getParameterValues("prices");
			action = request.getParameter("makeTransition");
			Date tmpDate = dateDAO.getLastDate();
			if (tmpDate != null) {
				request.setAttribute("date", dateFormat.format(tmpDate));
			}
			if (action == null || !action.equals("New Transition Begins")) {
				return "transition-day.jsp";
			}
			if (fundIds != null && fundIds.length != 0) {
				for (int i = 0; i < fundIds.length; i++) {
					if (prices[i] == null || prices[i].trim().length() == 0) {
						errors.add("You must fill in all the closing price !");
						break;
					}
				}

				double temp;
				for (int i = 0; i < prices.length; i++) {
					try {
						prices[i] = sanitize(prices[i].trim());
						temp = Double.parseDouble(prices[i]);
					} catch (NumberFormatException e) {
						errors.add("Closing price must be a valid number.");
						break;
					}

					if (temp < 1.0 || temp >= 1000.0) {
						errors.add("Closing price must be greater than $1 and less than $1,000.");
						break;
					}
				}
			}

			if (errors.size() != 0) {
				return "transition-day.jsp";
			}

			inputDate = sanitize(inputDate.trim());
			if (inputDate == null || inputDate.trim().length() == 0) {
				errors.add("You must input a date !");
				return "transition-day.jsp";
			}

			Date curDate;
			try {
				curDate = dateFormat.parse(inputDate);
			} catch (Exception e) {
				errors.add("Sorry, please input a valid date in format mm/dd/yyyy.");
				return "transition-day.jsp";
			}

			Date tmp = dateDAO.getLastDate();
			if (tmp != null) {
				if (!curDate.after(tmp)) {
					errors.add("The date must be later than the last transition day: " + dateFormat.format(tmp));
					return "transition-day.jsp";
				}
			}

			if (errors.size() != 0) {
				return "transition-day.jsp";
			}
			System.out.println("234");
			if (fundIds != null && fundIds.length != 0) {
				int[] ids = new int[fundIds.length];
				double[] amounts = new double[prices.length];

				for (int i = 0; i < fundIds.length; i++) {
					ids[i] = Integer.parseInt(fundIds[i]);
					amounts[i] = Double.parseDouble(prices[i]);
				}
				updatePrice(dateFormat.parse(inputDate), ids, amounts);
			}
			TransactionBean[] beans = transactionDAO.readPending();
			for (TransactionBean bean : beans) {
				doTransaction(bean, dateFormat.parse(inputDate));
			}
			System.out.println("xxx");
			DateBean dateBean = new DateBean();
			dateBean.setDate(dateFormat.parse(inputDate));
			dateBean.setId(1);
			if (dateDAO.getLastDate() == null) {

				System.out.println("bbb");
				dateDAO.create(dateBean);

			} else {
				System.out.println("ccc");
				dateDAO.update(dateBean);

			}
			request.setAttribute("message", "Transition completed.");
			request.setAttribute("date", dateFormat.format(dateDAO.getLastDate()));
			session.setAttribute("fundList", model.getFundList());

			return "transition-day.jsp";

		} catch (RollbackException e) {
			System.out.println(e.getMessage());
			errors.add("Another user is processing, please try again.");
			return "transition-day.jsp";
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			errors.add("Invalid Date format.");
			return "transition-day.jsp";
		}
	}

	// update all the fund closing price, process the transactions in queue.
	public void updatePrice(Date inputDate, int[] fundIds, double[] amounts) throws RollbackException {
		for (int i = 0; i < fundIds.length; i++) {
			PriceHistoryBean priceHistoryBean = new PriceHistoryBean();
			priceHistoryBean.setFundId(fundIds[i]);
			priceHistoryBean.setPrice((long) ((amounts[i]) * 100));
			priceHistoryBean.setPriceDate(inputDate);
			priceHistoryDAO.create(priceHistoryBean);
		}
	}

	// do the transactions one by one
	public void doTransaction(TransactionBean transactionBean, Date inputDate) throws RollbackException {
		String type = transactionBean.getTransactionType();

		int fundId = transactionBean.getFundId();
		int customerId = transactionBean.getCustomerId();
		Customer customer = customerDAO.getCustomerById(customerId);

		double shares = (double) transactionBean.getShares() / 1000.0;
		double amount = (double) transactionBean.getAmount() / 100.0;
		double cash = (double) customer.getCash() / 100.0;

		if (type.equals("Deposit Check")) {
			cash += amount;
		} else if (type.equals("Request Check")) {
			cash -= amount;
		} else if (type.equals("Buy")) {
			double price = (double) priceHistoryDAO.getPriceByFundIdAndDate(fundId, inputDate) / 100;
			cash -= amount;
			long curShares = (long) (amount / price * 1000);
			positionDAO.addShare(customerId, fundId, curShares);
			positionDAO.setPendingShare(customerId, fundId);
		} else if (type.equals("Sell")) {
			double price = (double) priceHistoryDAO.getPriceByFundIdAndDate(fundId, inputDate) / 100;
			cash += shares * price;
			long curShares = (long) (shares * 1000);
			positionDAO.addShare(customerId, fundId, -curShares);
			positionDAO.setPendingShare(customerId, fundId);
		}

		long curCash = (long) (cash * 100);
		customerDAO.setAmount(customer.getUserName(), curCash);
		customerDAO.setPendingAmount(customer.getUserName(), curCash);
		transactionDAO.setDate(transactionBean.getTransactionId(), inputDate);
	}

	private String sanitize(String s) {
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
	}
}
