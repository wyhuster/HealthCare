package dev.hust.leaf.healthcare.model;

public class DeviceState {
	private String UId;
	private String DvUId;
	private int OnOff;
	private int BatteryStatus;
	private double Lat;
	private double Lng;
	private String ReceiveDt;
	private String SendDt;

	public String getUId() {
		return UId;
	}

	public void setUId(String uId) {
		UId = uId;
	}

	public String getDvUId() {
		return DvUId;
	}

	public void setDvUId(String dvUId) {
		DvUId = dvUId;
	}

	public int getOnOff() {
		return OnOff;
	}

	public void setOnOff(int onOff) {
		OnOff = onOff;
	}

	public int getBatteryStatus() {
		return BatteryStatus;
	}

	public void setBatteryStatus(int batteryStatus) {
		BatteryStatus = batteryStatus;
	}

	public double getLat() {
		return Lat;
	}

	public void setLat(double lat) {
		Lat = lat;
	}

	public double getLng() {
		return Lng;
	}

	public void setLng(double lng) {
		Lng = lng;
	}

	public String getReceiveDt() {
		return ReceiveDt;
	}

	public void setReceiveDt(String receiveDt) {
		ReceiveDt = receiveDt;
	}

	public String getSendDt() {
		return SendDt;
	}

	public void setSendDt(String sendDt) {
		SendDt = sendDt;
	}
}
