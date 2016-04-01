package databean;

import org.genericdao.PrimaryKey;

@PrimaryKey("customerId,fundId")
public class PositionViewBean {
	private int customerId;
	private int fundId;
	private long shares;
	private long pendingShares;
	private long price;
	private String fundName;
	private String symbol;
	private double value;

	public int getFundId() {
		return fundId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public long getShares() {
		return shares;
	}

	public long getPendingShares() {
		return pendingShares;
	}

	public long getPrice() {
		return price;
	}

	public String getfundName() {
		return fundName;
	}

	public String getSymbol() {
		return symbol;
	}

	public double getValue() {
		return value;
	}

	public void setFundId(int i) {
		fundId = i;
	}
	
	public void setPrice(long l) {
		price = l;
	}

	public void setfundName(String s) {
		fundName = s;
	}

	public void setSymbol(String s) {
		symbol = s;
	}

	public void setCustomerId(int i) {
		customerId = i;
	}

	public void setShares(long l) {
		shares = l;
	}

	public void setPendingShares(long l) {
		pendingShares = l;
	}

	public void setValue(double d) {
		value = d;
	}
}
