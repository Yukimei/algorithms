package model;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.AmountHelper;
import databean.Customer;
import databean.Employee;

public class Model {
	private CustomerDAO customerDAO;
	private EmployeeDAO employeeDAO;
	private FundDAO fundDAO;
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
				employee.setFirstName("Jane");
				employee.setLastName("Admin");
				employee.setUserName("jadmin");
				employee.newPassword("admin");
				// employee.se
				employeeDAO.create(employee);
			} else
				employeeDAO = new EmployeeDAO("Employee", pool);
			fundDAO = new FundDAO("Fund", pool);
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

	public DateDAO getDateDAO() {
		return dateDAO;
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

	public int sellFundHelper(int fundId, int customerId, double shares) throws RollbackException {
		int flag = 0;
		try {
			Transaction.begin();

			long sharesLong = positionDAO.getShares(fundId, customerId);

			if (shares > (double) (sharesLong / 1000)) {
				return -1;
			}

			Customer customer = customerDAO.read(customerId);

			long priceLong = fundDAO.read(fundId).getInitial_value();
			long sharesChangeLong = (long) (shares * 1000);
			long cashChangeLong = (long) (priceLong * shares);
			long cashLong = customer.getCash();
			long availableCashLong = customer.getAvailableCash();

			cashLong = cashLong - cashChangeLong;
			availableCashLong = availableCashLong - cashChangeLong;

			customer.setCash(cashLong);
			customer.setAvailableCash(availableCashLong);
			customerDAO.update(customer);

			positionDAO.changeShare(customerId, fundId, -sharesChangeLong);

			Transaction.commit();
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}

	public int buyFundHelper(String userName, AmountHelper amount, String fundSymbol)
			throws RollbackException {
		int flag = 0;
		try {
			Transaction.begin();
			Customer customer = customerDAO.read(userName);
			if (customer == null) {
				throw new RollbackException("Customer " + userName + " doesn't exists.");
			}

			long amountLong = amount.getAmount();
			System.out.println(amountLong);

			if (amountLong < fundDAO.read(fundSymbol).getInitial_value()) {
				return -1;
			}
			System.out.println(fundDAO.read(fundSymbol).getInitial_value());

			int sharesAmount = (int) (amountLong / (fundDAO.read(fundSymbol).getInitial_value()));
			long priceLong = fundDAO.getPriceByFundSymbol(fundSymbol);

			System.out.println(sharesAmount);
			System.out.println(priceLong);
			System.out.println(customer.getAvailableCash());

			if (customer.getAvailableCash() < amountLong) {
				System.out.println(customer.getAvailableCash());
				System.out.println(amountLong);
				return -1;
			}

			customer.setAvailableCash(customer.getAvailableCash() - sharesAmount * priceLong);
			customer.setCash(customer.getCash() - sharesAmount * priceLong);
			customerDAO.update(customer);

			positionDAO.changeShare(customer.getCustomerId(), fundDAO.read(fundSymbol).getFundId(),
					sharesAmount * 1000);

			Transaction.commit();
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}
}