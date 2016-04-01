package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.Employee;

public class EmployeeDAO extends GenericDAO<Employee> {

	public EmployeeDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Employee.class, tableName, pool);
	}

	public int createEmployee(String userName, String firstName, String lastName, String password)
			throws RollbackException {
		int flag = 0;
		try {
			System.out.println("customerDAO++++ createEmployee");
			Transaction.begin();

			Employee employee = read(userName);

			if (employee != null) {
				return -1;
			}
			Employee user = new Employee();
			user.setUserName(userName);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.newPassword(password);
			create(user);
			System.out.println("employeeDAO++++ createCustomerLINE38");
			Transaction.commit();
			System.out.println("customerDAO++++ createCustomerLINE 39");
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}

	public Employee read(String userName) throws RollbackException {
		Employee[] employee = match(MatchArg.equals("userName", userName));

		return employee.length == 0 ? null : employee[0];
	}
}
