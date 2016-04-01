package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import databean.Employee;
import databean.FundBean;
import databean.PriceHistoryBean;
import databean.TransHistoryBean;
import databean.TransactionBean;
import databean.TransitionDayBean;

public class Model {
	private CustomerDAO customerDAO;
	private EmployeeDAO employeeDAO;
	private FundDAO fundDAO;
	private PriceHistoryDAO priceHistoryDAO;
	private PositionDAO positionDAO;
	private TransactionDAO transactionDAO;
	private DateDAO dateDAO;

	public Model(ServletConfig config) throws Exception {
		try {
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL = config.getInitParameter("jdbcURL");
			ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);

			positionDAO = new PositionDAO("Position", pool);
			transactionDAO = new TransactionDAO("Transaction", pool);

			customerDAO = new CustomerDAO("Customer", pool);
			dateDAO = new DateDAO("LastDate", pool);

			if (!tableExists(pool, "employee")) {
				employeeDAO = new EmployeeDAO("Employee", pool);
				Employee employee = new Employee();
				employee.setFirstName("Jeff");
				employee.setLastName("Eppinger");
				employee.setUserName("admin");
				employee.newPassword("admin");
				employeeDAO.create(employee);
			} else
				employeeDAO = new EmployeeDAO("Employee", pool);
			fundDAO = new FundDAO("Fund", pool);
			priceHistoryDAO = new PriceHistoryDAO("FundPriceHistory", pool);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}

	public CustomerDAO getCustomerDAO() {
		return customerDAO;
	}

	public EmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public FundDAO getFundDAO() {
		return fundDAO;
	}

	public TransactionDAO getTransactionDAO() {
		return transactionDAO;
	}

	public PositionDAO getPositionDAO() {
		return positionDAO;
	}

	public PriceHistoryDAO getPriceHistoryDAO() {
		return priceHistoryDAO;
	}

	public DateDAO getDateDAO() {
		return dateDAO;
	}

	public TransHistoryBean[] getTransactionList(int customerId) throws RollbackException {
		ArrayList<TransHistoryBean> transactionHistoryBeanList = new ArrayList<TransHistoryBean>();
		TransactionBean[] beans = transactionDAO.readByCustomerId(customerId);
		int i = 0;
		while (i < beans.length) {
			TransHistoryBean transHistoryBean = new TransHistoryBean();
			transHistoryBean.setTransactionType(beans[i].getTransactionType());
			transHistoryBean.setExecuteDate(beans[i].getExecuteDate());
			transHistoryBean.setShares(beans[i].getShares());
			transHistoryBean.setAmount(beans[i].getAmount());
			int fundId = beans[i].getFundId();
			if (fundId != 0) {
				FundBean fundBean = fundDAO.read(fundId);
				transHistoryBean.setSymbol(fundBean.getSymbol());
				transHistoryBean.setName(fundBean.getName());
				java.util.Date d = beans[i].getExecuteDate();
				if (d != null) {
					long price = priceHistoryDAO.getPriceByFundIdAndDate(fundId, new java.util.Date(d.getTime()));
					long shares = beans[i].getShares();
					long amount = beans[i].getAmount();
					double doublePrice = (double) price / 100.0;
					double doubleShares = (double) shares / 1000.0;
					double doubleAmount = (double) amount / 100.0;
					transHistoryBean.setPrice(price);
					if (beans[i].getTransactionType().equals("Buy")) {
						doubleShares = doubleAmount / doublePrice;
						transHistoryBean.setShares(Math.round(doubleShares * 1000.0));
					} else if (beans[i].getTransactionType().equals("Sell")) {
						doubleAmount = doublePrice * doubleShares;
						transHistoryBean.setAmount(Math.round(doubleAmount * 100.0));
					}
				}
			}
			i++;
			transactionHistoryBeanList.add(transHistoryBean);
		}
		Collections.sort(transactionHistoryBeanList,
				(a, b) -> priceHistoryDAO.compareDate(a.getExecuteDate(), b.getExecuteDate()));
		return transactionHistoryBeanList.toArray(new TransHistoryBean[transactionHistoryBeanList.size()]);
	}

	private boolean tableExists(ConnectionPool pool, String tableName) throws Exception {
		Connection con = null;
		try {
			con = pool.getConnection();
			DatabaseMetaData metaData = con.getMetaData();
			ResultSet rs = metaData.getTables(null, null, tableName, null);
			boolean answer = rs.next();
			rs.close();
			pool.releaseConnection(con);
			return answer;
		} catch (SQLException e) {
			try {
				if (con != null)
					con.close();
			} catch (SQLException e2) {
			}
			throw new Exception(e);
		}
	}

	// Get the fundId, name, ticker and previous closing price for the jsp to
	// display.
	public TransitionDayBean[] getFundList() throws RollbackException {
		FundBean[] fundBeans = fundDAO.getFunds();
		TransitionDayBean[] transitionDayBeans = new TransitionDayBean[fundBeans.length];
		int i = 0;
		while (i < fundBeans.length) {
			TransitionDayBean transitionDayBean = new TransitionDayBean();
			transitionDayBean.setFundId(fundBeans[i].getFundId());
			transitionDayBean.setName(fundBeans[i].getName());
			transitionDayBean.setSymbol(fundBeans[i].getSymbol());
			PriceHistoryBean[] priceHistoryBeans = priceHistoryDAO.read(fundBeans[i].getFundId());
			if (priceHistoryBeans.length > 0) {
				java.util.Date latestDate = priceHistoryBeans[0].getPriceDate();
				long price = priceHistoryBeans[0].getPrice();
				int j = 1;
				while (j < priceHistoryBeans.length) {
					java.util.Date curDate = priceHistoryBeans[j].getPriceDate();
					if (curDate.after(latestDate)) {
						price = priceHistoryBeans[j].getPrice();
						latestDate = curDate;
					}
					j++;

				}

				Date date = new Date(latestDate.getTime());
				transitionDayBean.setExecuteDate(date);
				transitionDayBean.setPrice(price);
			}
			transitionDayBeans[i] = transitionDayBean;
			i++;
		}
		return transitionDayBeans;
	}
}