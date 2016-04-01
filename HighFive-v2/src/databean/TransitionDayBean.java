package databean;

import java.util.Date;

public class TransitionDayBean {
	private int fundId;
	private String name;
	private String symbol;
	private Date executeDate;
	private long price;

	public int getFundId() {
		return fundId;
	}

	public String getName() {
		return name;
	}

	public Date getExecuteDate() {
		return executeDate;
	}

	public long getPrice() {
		return price;
	}

	public String getSymbol() {
		return symbol;
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

}
