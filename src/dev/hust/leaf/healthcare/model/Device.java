package dev.hust.leaf.healthcare.model;

public class Device {
	private String Code;
	private String Description;
	private int Enable;
	private int Hire;
	private String HireTime;
	private String ReturnTime;
	private int Type;
	private String TypeString;
	private String UId;

	public String getCode() {
		return Code;
	}

	public void setCode(String code) {
		Code = code;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getEnable() {
		return Enable;
	}

	public void setEnable(int enable) {
		Enable = enable;
	}

	public int getHire() {
		return Hire;
	}

	public void setHire(int hire) {
		Hire = hire;
	}

	public String getHireTime() {
		return HireTime;
	}

	public void setHireTime(String hireTime) {
		HireTime = hireTime;
	}

	public String getReturnTime() {
		return ReturnTime;
	}

	public void setReturnTime(String returnTime) {
		ReturnTime = returnTime;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public String getTypeString() {
		return TypeString;
	}

	public void setTypeString(String typeString) {
		TypeString = typeString;
	}

	public String getUId() {
		return UId;
	}

	public void setUId(String uId) {
		UId = uId;
	}

}
