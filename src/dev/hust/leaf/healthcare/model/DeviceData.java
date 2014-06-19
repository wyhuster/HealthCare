package dev.hust.leaf.healthcare.model;

public class DeviceData {

	private String DeviceUId;
	private int Flag;
	private int HeartRate;
	private long Id;
	private String ReceiveDt;
	private int SendMode;
	private String UId;
	private double[] Value;

	public String getDeviceUId() {
		return DeviceUId;
	}

	public void setDeviceUId(String deviceUId) {
		DeviceUId = deviceUId;
	}

	public int getFlag() {
		return Flag;
	}

	public void setFlag(int flag) {
		Flag = flag;
	}

	public int getHeartRate() {
		return HeartRate;
	}

	public void setHeartRate(int heartRate) {
		HeartRate = heartRate;
	}

	public long getId() {
		return Id;
	}

	public void setId(long id) {
		Id = id;
	}

	public String getReceiveDt() {
		return ReceiveDt;
	}

	public void setReceiveDt(String receiveDt) {
		ReceiveDt = receiveDt;
	}

	public int getSendMode() {
		return SendMode;
	}

	public void setSendMode(int sendMode) {
		SendMode = sendMode;
	}

	public String getUId() {
		return UId;
	}

	public void setUId(String uId) {
		UId = uId;
	}

	public double[] getValue() {
		return Value;
	}

	public void setValue(double[] value) {
		Value = value;
	}

}
