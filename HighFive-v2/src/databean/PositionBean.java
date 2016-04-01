package databean;

import org.genericdao.PrimaryKey;

@PrimaryKey("customerId,fundId")
public class PositionBean {
	private int customerId;
	private int fundId;
	private long shares;
	private long pendingShares;

	public int getFundId() {
		return fundId;
	}

	public long getShares() {
		return shares;
	}

	public long getPendingShares() {
		return pendingShares;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setFundId(int i) {
		fundId = i;
	}

	public void setShares(long l) {
		shares = l;
	}

	public void setPendingShares(long l) {
		pendingShares = l;
	}

	public void setCustomerId(int i) {
		customerId = i;
	}

}
