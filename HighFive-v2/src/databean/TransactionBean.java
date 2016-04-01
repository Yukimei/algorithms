package databean;

import java.util.Date;

import org.genericdao.PrimaryKey;

@PrimaryKey("transactionId")
public class TransactionBean {
	private int transactionId;
	private int customerId;
	private int fundId;
	private long shares;
	private String transactionType;
	private long amount;
	private Date executeDate;

	public int getTransactionId() {
		return transactionId;
	}

	public int getCustomerId() {
		return customerId;
	}

	public int getFundId() {
		return fundId;
	}

	public long getShares() {
		return shares;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public long getAmount() {
		return amount;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public void setTransactionId(int i) {
		transactionId = i;
	}

	public void setCustomerId(int i) {
		customerId = i;
	}

	public void setFundId(int i) {
		fundId = i;
	}

	public void setShares(long l) {
		shares = l;
	}

	public void setTransactionType(String s) {
		transactionType = s;
	}

	public void setAmount(long l) {
		amount = l;
	}

	public void setExecuteDate(Date d) {
		executeDate = d;
	}

}
