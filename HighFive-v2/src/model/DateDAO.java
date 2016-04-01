package model;

import java.util.Date;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;

import databean.DateBean;

public class DateDAO extends GenericDAO<DateBean> {

	public DateDAO(String tableName, ConnectionPool connectionPool) throws DAOException {
		super(DateBean.class, tableName, connectionPool);
	}

	public Date getLastDate() throws RollbackException {
		DateBean date = read(1);
		if (date != null)
			return date.getDate();
		else
			return null;
	}
}
