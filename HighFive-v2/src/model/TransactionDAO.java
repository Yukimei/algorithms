package model;

import org.genericdao.MatchArg;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;
import org.genericdao.ConnectionPool;
import org.genericdao.Transaction;

import databean.TransactionBean;

import java.util.Date;

public class TransactionDAO extends GenericDAO<TransactionBean> {
	public TransactionDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(TransactionBean.class, tableName, pool);
	}

	public TransactionBean read(int transactionId) throws RollbackException {
		TransactionBean[] beans = match(MatchArg.equals("transactionId", transactionId));
		return beans.length == 0? null : beans[0];
	}
	public TransactionBean[] readPending() throws RollbackException {
		TransactionBean[] beans = match(MatchArg.equals("executeDate", null));
		return beans;
	}

	public TransactionBean[] readAll() throws RollbackException {
		TransactionBean[] beans = match();
		return beans;
	}

	public TransactionBean[] readByCustomerId(int customerId) throws RollbackException {
		TransactionBean[] beans = match(MatchArg.equals("customerId", customerId));
		return beans;
	}

	public java.util.Date getLastDate(int customerId) throws RollbackException {
		TransactionBean[] beans = match(MatchArg.equals("customerId", customerId));
		if (beans.length > 0) {
			java.util.Date latest = null;
			for (int j = 0; j < beans.length; j++) {
				java.util.Date cur = beans[j].getExecuteDate();
				if (cur == null)
					continue;
				if (latest == null || cur.after(latest))
					latest = cur;
			}
			return latest;
		}
		return null;
	}

	public void add(TransactionBean transaction) throws RollbackException {
		try {
			Transaction.begin();
			createAutoIncrement(transaction);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}

	public void setDate(int transactionId, Date d) throws RollbackException {
		try {
			Transaction.begin();
			TransactionBean transaction = read(transactionId);
			if (transaction == null) {
				throw new RollbackException("Transaction doesn't exists.");
			}
			transaction.setExecuteDate(d);
			update(transaction);
			Transaction.commit();
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
	}
}
