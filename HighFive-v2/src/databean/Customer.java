package databean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.genericdao.PrimaryKey;

@PrimaryKey("customerId")
public class Customer {
	private int customerId;
	private String userName;
	private String hashedPassword;
	private String firstName;
	private String lastName;
	private String addrLine1;
	private String addrLine2;
	private String city;
	private String state;
	private String zip;
	private long cash;
	private long pendingCash;
	private int salt;

	public int getCustomerId() {
		return customerId;
	}

	public String getUserName() {
		return userName;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	public long getCash() {
		return cash;
	}

	public long getPendingCash() {
		return pendingCash;
	}

	public int getSalt() {
		return salt;
	}

	public void setCustomerId(int i) {
		customerId = i;
	}

	public void setUserName(String s) {
		userName = s;
	}

	public void setFirstName(String s) {
		firstName = s;
	}

	public void setLastName(String s) {
		lastName = s;
	}

	public void newPassword(String s) {
		salt = newSalt();
		hashedPassword = hash(s);
	}

	public void setHashedPassword(String s) {
		hashedPassword = s;
	}

	public void setAddrLine1(String s) {
		addrLine1 = s;
	}

	public void setAddrLine2(String s) {
		addrLine2 = s;
	}

	public void setCity(String s) {
		city = s;
	}

	public void setState(String s) {
		state = s;
	}

	public void setZip(String s) {
		zip = s;
	}

	public void setCash(long l) {
		cash = l;
	}

	public void setPendingCash(long l) {
		pendingCash = l;
	}

	public void setSalt(int i) {
		salt = i;
	}

	public boolean checkPwd(String password) {
		return hashedPassword.equals(hash(password));
	}

	private String hash(String clearPassword) {
		if (salt == 0)
			return null;

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError("Can't find the SHA1 algorithm in the java.security package");
		}

		String saltString = String.valueOf(salt);

		md.update(saltString.getBytes());
		md.update(clearPassword.getBytes());
		byte[] digestBytes = md.digest();

		StringBuffer digestSB = new StringBuffer();
		for (int i = 0; i < digestBytes.length; i++) {
			int lowNibble = digestBytes[i] & 0x0f;
			int highNibble = (digestBytes[i] >> 4) & 0x0f;
			digestSB.append(Integer.toHexString(highNibble));
			digestSB.append(Integer.toHexString(lowNibble));
		}
		String digestStr = digestSB.toString();

		return digestStr;
	}

	private int newSalt() {
		Random random = new Random();
		return random.nextInt(8192) + 1; // salt cannot be zero
	}
}
