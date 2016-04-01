package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import databean.PositionBean;

public class PositionDAO extends GenericDAO<PositionBean> {
	public PositionDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(PositionBean.class, tableName, pool);
	}

	public boolean ifOwnThisFund(int fundId, int customerId) throws RollbackException {
		PositionBean[] beans = match(
				MatchArg.and(MatchArg.equals("fundId", fundId), MatchArg.equals("customerId", customerId)));
		if (beans.length == 0) {
			return false;
		}
		return true;
	}

	public long getShares(int fundId, int customerId) throws RollbackException {
		PositionBean[] beans = match(
				MatchArg.and(MatchArg.equals("fundId", fundId), MatchArg.equals("customerId", customerId)));
		if (beans.length == 0) {
			return -1;
		}
		return beans[0].getShares();
	}

	public void changeShare(int customerId, int fundId, long shares) throws RollbackException {
		PositionBean[] beans = match();
		int flag = 0;
		for (int i = 0; i < beans.length; i++) {
			System.out.println("change share");
			if (customerId == beans[i].getCustomerId() && fundId == beans[i].getFundId()) {
				flag = 1;
				// try {
				long newShares = beans[i].getShares() + shares;
				if (newShares > 0) {
					beans[i].setShares(newShares);
					update(beans[i]);
				} else {
					delete(beans[i].getFundId(), beans[i].getCustomerId());
				}
			}
		}
		if (flag == 0) {
			PositionBean bean = new PositionBean();
			bean.setCustomerId(customerId);
			bean.setFundId(fundId);
			System.out.print("fund ID is " + fundId);
			bean.setShares(shares);
			create(bean);
		}
	}

	public PositionBean[] getPositionList(int customerId) throws RollbackException {
		return match(MatchArg.equals("customerId", customerId));
	}
}
