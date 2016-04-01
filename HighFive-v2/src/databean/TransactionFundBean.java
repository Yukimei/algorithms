package databean;

import java.util.Date;

public class TransactionFundBean {
	private int customerId;
	private int fundId;
	private String name;
	private String symbol;
	private long shares;
	private long pendingShares;
	private long price;
	private Date executeDate;

	public int getCustomerId() {
		return customerId;
	}

	public int getFundId() {
		return fundId;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public long getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public String getSymbol() {
		return symbol;
	}

	public long getShares() {
		return shares;
	}

	public long getPendingShares() {
		return pendingShares;
	}

	public void setCustomerId(int i) {
		customerId = i;
	}

	public void setFundId(int i) {
		fundId = i;
	}

	public void setExecuteDate(Date d) {
		executeDate = d;
	}

	public void setPrice(long l) {
		price = l;
	}

	public void setName(String s) {
		name = s;
	}

	public void setSymbol(String s) {
		symbol = s;
	}

	public void setShares(long l) {
		shares = l;
	}

	public void setPendingShares(long l) {
		pendingShares = l;
	}

}
