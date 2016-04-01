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

	public FundBean read(String symbol) throws RollbackException {
		FundBean[] funds = match(MatchArg.equals("symbol", symbol));
		return funds.length == 0 ? null : funds[0];
	}

	public FundBean[] getFunds() throws RollbackException {
		FundBean[] funds = match();
		return funds;
	}

	public long getPriceByFundSymbol(String symbol) throws RollbackException {
		FundBean[] beans = match(MatchArg.equals("symbol", symbol));
		long price = beans[0].getInitial_value();
		return price;
	}

	public int createFundHelper(String name, String symbol, double initial_value) throws RollbackException {
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
			bean.setInitial_value((long) (initial_value) * 100);

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

	public int updateFundPriceHelper() {
		// check the price within 10% range fluctuation and update price
		try {

			FundBean[] fundBean = getFunds();

			for (FundBean fund : fundBean) {
				long newPrice = (long) (fund.getInitial_value() * (1 + 0.2 * (Math.random() - 0.5)));
				fund.setInitial_value(newPrice);
				update(fund);
			}
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}
}
