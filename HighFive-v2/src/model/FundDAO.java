package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.FundBean;

public class FundDAO extends GenericDAO<FundBean> {
	public FundDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(FundBean.class, tableName, pool);
	}

	public FundBean read(int fundId) throws RollbackException {
		FundBean[] funds = match(MatchArg.equals("fundId", fundId));
		return funds.length == 0 ? null : funds[0];
	}

	public FundBean[] getFunds() throws RollbackException {
		FundBean[] funds = match();
		return funds;
	}
	public int createFundHelper(String name, String symbol) throws RollbackException {
		int flag = 0;
		try {
			Transaction.begin();
			
			if (nameExist(name)) {
				return -1;
			}
			
			if (symbolExist(symbol)) {
				return -2;
			}
			
			FundBean bean = new FundBean();
			bean.setName(name);
			bean.setSymbol(symbol);
			
			createAutoIncrement(bean);
			Transaction.commit();
			flag = 1;
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}

	public boolean nameExist(String name) throws RollbackException {
		FundBean[] funds = match(MatchArg.equals("name", name));
		return funds.length == 0 ? false : true;
	}

	public boolean symbolExist(String symbol) throws RollbackException {
		FundBean[] funds = match(MatchArg.equals("symbol", symbol));
		return funds.length == 0 ? false : true;
	}

	@Override
	public void createAutoIncrement(FundBean fund) throws RollbackException {
			super.createAutoIncrement(fund);
	}
}
