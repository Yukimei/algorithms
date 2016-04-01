package databean;
import java.util.Date;

import org.genericdao.PrimaryKey;
@PrimaryKey("id")
public class DateBean {
	private Date date;
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDate() {return date;}
	public void setDate(Date d) {date = d;}
}
