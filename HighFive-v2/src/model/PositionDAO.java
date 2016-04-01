package model;

import java.util.ArrayList;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databean.PositionBean;

public class PositionDAO extends GenericDAO<PositionBean> {
	public PositionDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(PositionBean.class, tableName, pool);
	}
	
	
	public int sellFundHelper(int fundId, int customerId, double shares) throws RollbackException {
		int flag = 0;
		try {
			Transaction.begin();
			long pendingShares = getPendingShares(fundId, customerId).getPendingShares();
			if (shares > (double) (pendingShares / 1000)) {
				return -1;
			} 
			pendingShares -= (long) (shares * 1000);

			PositionBean updateShare = new PositionBean();
			updateShare.setCustomerId(customerId);
			updateShare.setFundId(fundId);
			updateShare.setShares(getPendingShares(fundId, customerId).getShares());
			updateShare.setPendingShares(pendingShares);
			update(updateShare);
			
			Transaction.commit();
			flag = 1;
			
		} finally {
			if (Transaction.isActive())
				Transaction.rollback();
		}
		return flag;
	}
	
	public boolean existName(int fundId) throws RollbackException {
		PositionBean[] beans = match(MatchArg.equals("fundId", fundId));
		return beans.length == 0 ? false : true;
	}

	public PositionBean getPendingShares(int fundId, int customerId) throws RollbackException {
		PositionBean[] positions = match(MatchArg.and(MatchArg.equals("fundId", fundId), MatchArg.equals("customerId", customerId)));
		if (positions.length == 0) return null;
		return positions[0];
	}

	public void addShare(int customerId, int fundId, long shares) throws RollbackException {
		PositionBean[] beans = match();
		boolean done = false;
		for (int i = 0; i < beans.length; i++) {
			if (beans[i].getFundId() == fundId && beans[i].getCustomerId() == customerId) {
				done = true;
				try {
					Transaction.begin();

					long tmp = beans[i].getShares() + shares;
					if (tmp > 0) {
						beans[i].setShares(tmp);
						update(beans[i]);
					} else {
						delete(beans[i].getFundId(), beans[i].getCustomerId());
					}
					Transaction.commit();
				} finally {
					if (Transaction.isActive())
						Transaction.rollback();
				}
			}
		}
		if (!done) {
			PositionBean bean = new PositionBean();
			bean.setCustomerId(customerId);
			bean.setFundId(fundId);
			bean.setShares(shares);
			create(bean);
		}

	}

	public void setPendingShare(int customerId, int fundId) throws RollbackException {
		PositionBean[] position = null;
		position = match(MatchArg.and(MatchArg.equals("fundId", fundId), MatchArg.equals("customerId", customerId)));
		if (position == null)
			return;

		Transaction.begin();
		long tmp = position[0].getShares();
		if (tmp > 0) {
			position[0].setPendingShares(tmp);
			update(position[0]);
		} else {
			delete(position[0].getFundId(), position[0].getCustomerId());
		}
		Transaction.commit();
	}

	public ArrayList<Integer> getSharesByCustomerId(int customerId) throws RollbackException {
		PositionBean[] beans = match(MatchArg.equals("customerId", customerId));
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < beans.length; i++) {
			ids.add(beans[i].getFundId());
		}
		return ids;
	}

	public PositionBean[] getPositionListByCustomerId(int customerId) throws RollbackException {
		return match(MatchArg.equals("customerId", customerId));
	}
}
