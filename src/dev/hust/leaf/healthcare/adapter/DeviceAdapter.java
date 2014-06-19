package dev.hust.leaf.healthcare.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.activity.DataQueryActivity;
import dev.hust.leaf.healthcare.activity.ExceptionActivity;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.Device;
import dev.hust.leaf.healthcare.model.DeviceState;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceAdapter extends BaseAdapter {

	private List<Device> devices;
	private Context context;
	private LayoutInflater mInflater;

	public DeviceAdapter(List<Device> devices, Context context) {
		if (devices == null) {
			this.devices = new ArrayList<Device>();
		} else {
			this.devices = devices;
		}
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return devices.size();
	}

	@Override
	public Device getItem(int position) {

		return devices.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View row = convertView;
		ViewHolder holder;

		if (row == null) {
			row = mInflater.inflate(R.layout.item_device, parent, false);
			holder = new ViewHolder();
			holder.tv_device_id = (TextView) row
					.findViewById(R.id.tv_device_id);
			holder.tv_device_type = (TextView) row
					.findViewById(R.id.tv_device_type);
			holder.tv_device_enable = (TextView) row
					.findViewById(R.id.tv_device_enable);
			holder.tv_device_hire = (TextView) row
					.findViewById(R.id.tv_device_hire);
			holder.tv_device_hire_time = (TextView) row
					.findViewById(R.id.tv_device_hire_time);
			holder.tv_device_return_time = (TextView) row
					.findViewById(R.id.tv_device_return_time);
			holder.tv_device_desc = (TextView) row
					.findViewById(R.id.tv_device_des);

			holder.row_device_hire_time = row
					.findViewById(R.id.row_device_hire_time);
			holder.row_device_return_time = row
					.findViewById(R.id.row_device_return_time);

			holder.btn_device_state = (TextView) row
					.findViewById(R.id.btn_device_state);
			holder.btn_data_query = (TextView) row
					.findViewById(R.id.btn_data_query);
			holder.btn_exception = (TextView) row
					.findViewById(R.id.btn_exception);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final Device dev = devices.get(position);
		holder.tv_device_id.setText(dev.getCode());
		holder.tv_device_type.setText(dev.getTypeString());
		holder.tv_device_desc.setText(dev.getDescription());
		if (dev.getEnable() == 1) {
			holder.tv_device_enable.setText("使用中");
		} else {
			holder.tv_device_enable.setText("未使用");
		}
		if (dev.getHire() == 0) {
			holder.tv_device_hire.setText("非租借");
			holder.row_device_hire_time.setVisibility(View.GONE);
			holder.row_device_return_time.setVisibility(View.GONE);
		} else {
			holder.tv_device_hire.setText("是租借");
			if (dev.getHireTime() != null) {
				holder.row_device_hire_time.setVisibility(View.VISIBLE);
				holder.tv_device_hire_time.setText(dev.getHireTime());
			}
			if (dev.getReturnTime() != null) {
				holder.row_device_return_time.setVisibility(View.VISIBLE);
				holder.tv_device_return_time.setText(dev.getReturnTime());
			}
		}
		OnClickListener listener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.btn_device_state:
					getDeviceState(dev.getCode(), dev.getUId());
					// Toast.makeText(context, "btn_device_state" + position,
					// Toast.LENGTH_SHORT).show();
					break;
				case R.id.btn_data_query:
					Intent i1 = new Intent(context, DataQueryActivity.class);
					i1.putExtra("deviceCode", dev.getCode());
					i1.putExtra("deviceUid", dev.getUId());
					context.startActivity(i1);
					break;
				case R.id.btn_exception:
					Intent i2 = new Intent(context, ExceptionActivity.class);
					i2.putExtra("deviceCode", dev.getCode());
					i2.putExtra("deviceUid", dev.getUId());
					context.startActivity(i2);
					break;
				}

			}
		};
		holder.btn_device_state.setOnClickListener(listener);
		holder.btn_data_query.setOnClickListener(listener);
		holder.btn_exception.setOnClickListener(listener);

		return row;
	}

	private void getDeviceState(final String devCode, String devUid) {
		final ProgressDialog progress = new ProgressDialog(context);
		progress.setMessage("加载中...");
		RequestParams params = new RequestParams();
		params.put("deviceUid", devUid);
		RestClient.get(RestClient.URL_GET_DEVICE_STATE, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						progress.show();
					}

					@Override
					public void onSuccess(String response) {
						try {
							DeviceState state = JSON.parseArray(response,
									DeviceState.class).get(0);
							if (state != null)
								showStateDialog(devCode, state);

						} catch (Exception ex) {
							try {
								JSONObject json = new JSONObject(response);
								if (json.has("error_msg")) {
									Toast.makeText(context,
											json.getString("error_msg"),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception ex1) {
								Toast.makeText(context,
										"error:" + ex1.getMessage(),
										Toast.LENGTH_SHORT).show();
								ex1.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(context, "error:" + error.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						progress.dismiss();
					}
				});

	}

	private void showStateDialog(String devCode, DeviceState state) {
		LayoutInflater inflaterDl = LayoutInflater.from(context);
		View layout = inflaterDl.inflate(R.layout.layout_device_state, null);

		Dialog dialog = new Dialog(context);
		dialog.setTitle(devCode + "设备状态");
		dialog.setContentView(layout);

		TextView tv_onoff = (TextView) layout.findViewById(R.id.tv_state_onoff);
		TextView tv_battery = (TextView) layout
				.findViewById(R.id.tv_state_battery);
		TextView tv_longitude = (TextView) layout
				.findViewById(R.id.tv_state_longitude);
		TextView tv_latitude = (TextView) layout
				.findViewById(R.id.tv_state_latitude);
		TextView tv_receive_time = (TextView) layout
				.findViewById(R.id.tv_state_receive_time);
		TextView tv_sent_time = (TextView) layout
				.findViewById(R.id.tv_state_sent_time);
		if (state.getOnOff() == 1)
			tv_onoff.setText("是");
		else
			tv_onoff.setText("否");
		tv_battery.setText("" + state.getBatteryStatus());
		DecimalFormat df = new java.text.DecimalFormat("#0.00");
		tv_longitude.setText("" + df.format(state.getLng()));
		tv_latitude.setText("" + df.format(state.getLat()));
		tv_receive_time.setText(state.getReceiveDt());
		tv_sent_time.setText(state.getSendDt());

		dialog.show();
	}

	private class ViewHolder {
		TextView tv_device_id;
		TextView tv_device_type;
		TextView tv_device_desc;
		TextView tv_device_hire;
		TextView tv_device_enable;
		View row_device_hire_time;
		View row_device_return_time;
		TextView tv_device_hire_time;
		TextView tv_device_return_time;
		TextView btn_device_state;
		TextView btn_data_query;
		TextView btn_exception;
	}

}
