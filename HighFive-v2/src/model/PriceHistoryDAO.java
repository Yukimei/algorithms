package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import databean.PriceHistoryBean;

public class PriceHistoryDAO extends GenericDAO<PriceHistoryBean> {
	public PriceHistoryDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(PriceHistoryBean.class, tableName, pool);
	}

	public PriceHistoryBean[] read(int fundId) throws RollbackException {
		PriceHistoryBean[] beans = match(MatchArg.equals("fundId", fundId));
		if (beans.length == 0) return beans;
		Arrays.sort(beans, new Comparator<PriceHistoryBean>() {
			public int compare(PriceHistoryBean a, PriceHistoryBean b) {
				return compareDate(a.getPriceDate(), b.getPriceDate());
			}
		});
		return beans;
	}

	public long getPriceByFundId(int fundId) throws RollbackException {
		PriceHistoryBean[] beans = match(MatchArg.equals("fundId", fundId));
		if (beans.length > 0) {
			java.util.Date latest = beans[0].getPriceDate();
			long price = beans[0].getPrice();
			for (int j = 1; j < beans.length; j++) {
				java.util.Date cur = beans[j].getPriceDate();
				if (cur.after(latest)) {
					price = beans[j].getPrice();
					latest = cur;
				}
			}
			return price;
		}
		return 0;
	}

	public long getPriceByFundIdAndDate(int fundId, Date d) throws RollbackException {
		PriceHistoryBean[] beans = match(MatchArg.and(MatchArg.equals("fundId", fundId), MatchArg.equals("priceDate", d)));
		return (beans.length > 0) ? beans[0].getPrice() : 0;
	}
	
    public java.util.Date getAllLastDate() throws RollbackException {
        PriceHistoryBean[] beans = match();
        if(beans.length > 0){
            java.util.Date latest = null;
            for(int j = 0; j < beans.length; j ++){
                java.util.Date cur = beans[j].getPriceDate();
                if ( cur == null) 
                	continue;
                if ( latest == null || cur.after(latest) )
                    latest = cur;  
            }
            return latest;
        }
        return null;
    }
	
	public int compareDate(java.util.Date a, java.util.Date b) {
		if (a == null) return -1;
		if (b == null) return 1;
		if (a.after(b)) return -1;
		else return 1;
	}
}
