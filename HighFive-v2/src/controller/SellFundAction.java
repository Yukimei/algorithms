package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databean.Customer;
import databean.PositionBean;
import databean.PositionViewBean;
import databean.TransactionBean;
import formbean.SellFundForm;
import model.CustomerDAO;
import model.FundDAO;
import model.Model;
import model.PositionDAO;
import model.PriceHistoryDAO;
import model.TransactionDAO;

public class SellFundAction extends Action {
	private FormBeanFactory<SellFundForm> formBeanFactory = FormBeanFactory.getInstance(SellFundForm.class);

	private TransactionDAO transactionDAO;
	private PriceHistoryDAO priceHistoryDAO;
	private PositionDAO positionDAO;
	private CustomerDAO customerDAO;
	private FundDAO fundDAO;

	public SellFundAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		priceHistoryDAO = model.getPriceHistoryDAO();
		positionDAO = model.getPositionDAO();
		customerDAO = model.getCustomerDAO();
		fundDAO = model.getFundDAO();
	}

	public String getName() {
		return "sell_fund.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		// check whether user is customer
		HttpSession session = request.getSession();

		boolean isCustomer = (boolean) session.getAttribute("isCustomer");

		Customer customer = (Customer) session.getAttribute("customer");

		if (!isCustomer) {
			errors.add("You are not eligible to buy fund for a customer");
			return "customer-list.jsp?page=1";
		}

		// check whether user is customer

		if (customer == null) {
			return "login_customer.do";
		}

		try {
			request.setAttribute("positionList", getPositionList(customer.getCustomerId()));

			// check whether the form is clicked
			SellFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			// if no parm were passed, return with no erros so that the form
			// will be presented
			if (!form.isPresent()) {
				return "sell-fund.jsp";
			}

			// any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "sell-fund.jsp";
			}

			String fundId = request.getParameter("fundId");
			String shares = request.getParameter("shares");
			int id = Integer.parseInt(fundId);

			try {
				if (fundDAO.read(id) == null) {
					errors.add("Fund Id: " + id + " doesn't exist.");
				}
			} catch (RollbackException e) {
				e.printStackTrace();
			}

			PositionBean position = positionDAO.getPendingShares(id, customer.getCustomerId());
			if (position == null) {
				errors.add("Customer does not own this Fund.");
				return "sell-fund.jsp";
			}

			if (errors.size() != 0) {
				PositionBean updateShares = positionDAO.getPendingShares(id, customer.getCustomerId());
				session.setAttribute("pendingShares", String.valueOf(updateShares.getPendingShares()));
				return "sell-fund.jsp";
			}

			// sell fund action
			customer = customerDAO.read(customer.getCustomerId());
			request.setAttribute("customer", customer);

			double num_shares = Double.parseDouble(shares);

			if (num_shares <= 0) {
				errors.add("The amount has to be positive.");
				PositionBean updateShares = positionDAO.getPendingShares(id, customer.getCustomerId());
				session.setAttribute("pendingShares", String.valueOf(updateShares.getPendingShares()));
				return "sell-fund.jsp";
			}

			else if (num_shares <= 0.001 || num_shares >= 100000.0) {
				errors.add("The amount must be greater than 0.001 and less than 100,000.");
				PositionBean updateShares = positionDAO.getPendingShares(id, customer.getCustomerId());
				session.setAttribute("pendingShares", String.valueOf(updateShares.getPendingShares()));
				return "sell-fund.jsp";
			}

			int flag = positionDAO.sellFundHelper(id, customer.getCustomerId(), num_shares);
			if (flag == -1) {
				errors.add("Shares requested is greater than available shares");
				PositionBean updateShares = positionDAO.getPendingShares(id, customer.getCustomerId());
				session.setAttribute("pendingShares", String.valueOf(updateShares.getPendingShares()));
				return "sell-fund.jsp";
			}
			if (flag == 1) {
				// update transaction bean

				TransactionBean trans = new TransactionBean();
				trans.setCustomerId(customer.getCustomerId());
				trans.setFundId(id);
				trans.setShares((long) (num_shares * 1000));
				trans.setTransactionType("Sell");
				transactionDAO.add(trans);
			}
			//

			PositionBean updateShares = positionDAO.getPendingShares(id, customer.getCustomerId());
			session.setAttribute("pendingShares", String.valueOf(updateShares.getPendingShares()));

			request.setAttribute("message", "Dear " + customer.getFirstName() + " you sold "
					+ (double) ((long) (num_shares * 1000)) / 1000 + " number of for fund " + id + ".");

			request.setAttribute("positionList", getPositionList(customer.getCustomerId()));

			return "sell-fund.jsp";
		} catch (FormBeanException e) {
			errors.add("Input format error");

			return "sell-fund.jsp";
		} catch (RollbackException e) {
			errors.add("Sorry, your transaction is in pending state. Please try again later.");
			return "sell-fund.jsp";
		}
	}

	public PositionViewBean[] getPositionList(int customerId) throws RollbackException {
		PositionBean[] positionList = positionDAO.getPositionListByCustomerId(customerId);
		PositionViewBean[] positionViewlist = new PositionViewBean[positionList.length];
		for (int i = 0; i < positionList.length; i++) {
			PositionViewBean positionViewBean = new PositionViewBean();
			positionViewBean.setFundId(positionList[i].getFundId());
			positionViewBean.setfundName(fundDAO.read(positionList[i].getFundId()).getName());
			positionViewBean.setSymbol(fundDAO.read(positionList[i].getFundId()).getSymbol());
			positionViewBean.setPendingShares(positionList[i].getPendingShares());
			positionViewBean.setShares(positionList[i].getShares());
			positionViewBean.setShares(positionList[i].getShares());
			int fundid = positionList[i].getFundId();
			positionViewBean.setPrice(priceHistoryDAO.getPriceByFundId(fundid));
			positionViewlist[i] = positionViewBean;
		}
		return positionViewlist;
	}

}
