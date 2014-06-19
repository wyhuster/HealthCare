package dev.hust.leaf.healthcare.model;

public class User {
	private String CreateDt;
	private String Email;
	private String Phone;
	private byte[] Photo;
	private String RealName;
	private int Sex;
	private String SexString;
	private String UserName;
	private int UserType;
	private String UserTypeString;
	private String UserUId;

	public String getCreateDt() {
		return CreateDt;
	}

	public void setCreateDt(String createDt) {
		CreateDt = createDt;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPhone() {
		return Phone;
	}

	public void setPhone(String phone) {
		Phone = phone;
	}

	public byte[] getPhoto() {
		return Photo;
	}

	public void setPhoto(byte[] photo) {
		Photo = photo;
	}

	public String getRealName() {
		return RealName;
	}

	public void setRealName(String realName) {
		RealName = realName;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public String getSexString() {
		return SexString;
	}

	public void setSexString(String sexString) {
		SexString = sexString;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public int getUserType() {
		return UserType;
	}

	public void setUserType(int userType) {
		UserType = userType;
	}

	public String getUserTypeString() {
		return UserTypeString;
	}

	public void setUserTypeString(String userTypeString) {
		UserTypeString = userTypeString;
	}

	public String getUserUId() {
		return UserUId;
	}

	public void setUserUId(String userUId) {
		UserUId = userUId;
	}

}
