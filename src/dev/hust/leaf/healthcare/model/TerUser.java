package dev.hust.leaf.healthcare.model;

public class TerUser {
	private String Address;
	private String BirthDt;
	private String IdentityCode;
	private String Memo; // ±¸×¢
	private String Occupation;// Ö°Òµ

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getBirthDt() {
		return BirthDt;
	}

	public void setBirthDt(String birthDt) {
		BirthDt = birthDt;
	}

	public String getIdentityCode() {
		return IdentityCode;
	}

	public void setIdentityCode(String identityCode) {
		IdentityCode = identityCode;
	}

	public String getMemo() {
		return Memo;
	}

	public void setMemo(String memo) {
		Memo = memo;
	}

	public String getOccupation() {
		return Occupation;
	}

	public void setOccupation(String occupation) {
		Occupation = occupation;
	}
}
