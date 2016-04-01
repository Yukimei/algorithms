package databean;

import java.util.Date;

public class TransHistoryBean {

	private String name = "";
	private Date executeDate;
	private long shares;
	private String symbol;
	private String transactionType;
	private long amount;
	private long price;

	public String getName() {
		return name;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public long getShares() {
		return shares;
	}

	public String getSymbol() {
		return symbol;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public long getAmount() {
		return amount;
	}

	public long getPrice() {
		return price;
	}

	public void setName(String s) {
		name = s;
	}

	public void setExecuteDate(Date d) {
		executeDate = d;
	}

	public void setShares(long l) {
		shares = l;
	}

	public void setSymbol(String s) {
		symbol = s;
	}

	public void setTransactionType(String s) {
		transactionType = s;
	}

	public void setAmount(long l) {
		amount = l;
	}

	public void setPrice(long l) {
		price = l;
	}
}
