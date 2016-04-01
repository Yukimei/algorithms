package databean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.genericdao.PrimaryKey;

@PrimaryKey("userName")
public class Employee {
	private String userName;
	private String hashedPassword;
	private String firstName;
	private String lastName;
	private int salt;

	public String getUserName() {
		return userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public int getSalt() {
		return salt;
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setUserName(String s) {
		userName = s;
	}

	public void setHashedPassword(String s) {
		hashedPassword = s;
	}

	public void setLastName(String s) {
		lastName = s;
	}

	public void newPassword(String s) {
		salt = newSalt();
		hashedPassword = hash(s);
	}

	public void setSalt(int i) {
		salt = i;
	}

	public void setFirstName(String s) {
		firstName = s;
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
		return random.nextInt(8192) + 1;
	}
}
