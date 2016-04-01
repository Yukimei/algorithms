package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.AmountHelper;
import databean.Customer;
import databean.TransactionBean;

public class CustomerDAO extends GenericDAO<Customer> {

	public CustomerDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Customer.class, tableName, pool);
	}

	public Customer read(String userName) throws RollbackException {
		Customer[] customer = match(MatchArg.equals("userName", userName));
		if (customer.length != 0)
			return customer[0];
		return null;
	}

	public Customer[] getAllCustomers() throws RollbackException {
		Customer[] customers = match();
		return customers;
	}

	public void updateCashAndAvailableCash(int userId, long cash) throws RollbackException {
		try {
			Transaction.begin();
			Customer customer = read(userId);
			if (customer == null) {
				throw new RollbackException("User " + userId + " doesn't exist.");
			}
			customer.setAvailableCash(cash);
			customer.setCash(cash);
			update(customer);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public int updateAvailableCash(String userName, AmountHelper amount) throws RollbackException {
		int flag = -1;
		try {
			Transaction.begin();
			Customer customer = read(userName);
			if (customer == null) {
				throw new RollbackException("Customer " + userName + " doesn't exists.");
			}

			if (customer.getAvailableCash() < amount.getAmount()) {
				System.out.println(customer.getAvailableCash());
				System.out.println(amount.getAmount());
				return -1;
			}

			customer.setAvailableCash(customer.getAvailableCash() - amount.getAmount());
			update(customer);

			Transaction.commit();
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}

	public int createCustomer(String userName, String firstName, String lastName, String password, String addr1,
			String addr2, String city, String state, String zip) throws RollbackException {
		int flag = 0;
		try {
			System.out.println("customerDAO++++ createCustomer");
			Transaction.begin();

			Customer customer = read(userName);

			if (customer != null) {
				return -1;
			}
			Customer user = new Customer();
			user.setUserName(userName);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.newPassword(password);
			user.setAddrLine1(addr1);
			user.setAddrLine2(addr2);
			user.setCity(city);
			user.setState(state);
			user.setZip(zip);
			user.setCash(0);

			createAutoIncrement(user);
			Transaction.commit();
			System.out.println("customerDAO++++ createCustomer222");
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}

	@Override
	public void createAutoIncrement(Customer customer) throws RollbackException {
		super.createAutoIncrement(customer);
	}

	private void doTransaction(TransactionBean transactionBean) throws RollbackException {
		String type = transactionBean.getTransactionType();

		Customer customer = read(transactionBean.getCustomerId());

		long cashChange = transactionBean.getAmount();
		long cash = customer.getCash();

		if (type.equals("Deposit")) {
			cash = cash + cashChange;
		} else if (type.equals("Request")) {
			cash = cash - cashChange;
		}
		updateCashAndAvailableCash(customer.getCustomerId(), cash);
	}

	public int doTransactionHelper(TransactionDAO transactionDAO) {
		try {

				TransactionBean[] beans = transactionDAO.read();
				for (TransactionBean bean : beans) {
					doTransaction(bean);
					int id = bean.getTransactionId();
					// key point here, delete table
					transactionDAO.delete(id);

				}
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
}
