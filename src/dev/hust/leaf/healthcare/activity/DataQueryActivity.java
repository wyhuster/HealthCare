package dev.hust.leaf.healthcare.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.res.Configuration;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.DatePicker;

import android.widget.FrameLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;

import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.DeviceData;
import dev.hust.leaf.healthcare.utils.DeviceDataDialog;
import dev.hust.leaf.healthcare.utils.TimeChart;

public class DataQueryActivity extends SherlockActivity {

	private final static int TYPE_GET_NEWLY_DATA = 0;
	private final static int TYPE_GET_DATA_LIST = 1;
	private final static int TYPE_GET_DATA_DETAIL = 2;

	private ProgressDialog progressdialog;
	private View progress;
	private View layout_content;

	private TextView tv_data_title;
	private TextView tv_heart;
	private TextView tv_send_mode;
	private TextView tv_data_state;
	private TextView tv_data_receive_time;

	private TextView tv_query_starttime;
	private TextView tv_query_endtime;
	private Button btn_query;
	private FrameLayout layout_chart;

	private Calendar calendar;

	private String deviceUid;
	private String dataUid;

	private DeviceDataDialog dataListDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_data_query);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		String deviceCode = getIntent().getStringExtra("deviceCode");
		this.setTitle("设备" + deviceCode + "-心电数据");
		deviceUid = getIntent().getStringExtra("deviceUid");
		progress = findViewById(R.id.progress_bar);
		layout_content = findViewById(R.id.layout_content);

		tv_data_title = (TextView) findViewById(R.id.tv_data_title);
		tv_heart = (TextView) findViewById(R.id.tv_heart_rate);
		tv_send_mode = (TextView) findViewById(R.id.tv_send_mode);
		tv_data_state = (TextView) findViewById(R.id.tv_data_state);
		tv_data_receive_time = (TextView) findViewById(R.id.tv_data_receive_time);

		calendar = Calendar.getInstance();
		String str_date = calendar.get(Calendar.YEAR) + "-"
				+ (calendar.get(Calendar.MONTH) + 1) + "-"
				+ calendar.get(Calendar.DAY_OF_MONTH);
		tv_query_starttime = (TextView) findViewById(R.id.tv_data_starttime);
		tv_query_endtime = (TextView) findViewById(R.id.tv_data_endtime);
		tv_query_starttime.setText(str_date);
		tv_query_endtime.setText(str_date);
		btn_query = (Button) findViewById(R.id.btn_data_query);

		tv_query_starttime.setOnClickListener(clickListener);
		tv_query_endtime.setOnClickListener(clickListener);
		btn_query.setOnClickListener(clickListener);

		layout_chart = (FrameLayout) findViewById(R.id.frame_chart);

		progressdialog = new ProgressDialog(this);
		progressdialog.setMessage("加载中...");
		progressdialog.setCancelable(false);

		getData(TYPE_GET_NEWLY_DATA);

	}

	private DatePickerDialog.OnDateSetListener dateListener1 = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year, int month,
				int dayOfMonth) {
			String text = year + "-" + (month + 1) + "-" + dayOfMonth;
			tv_query_starttime.setText(text);
		}
	};
	private DatePickerDialog.OnDateSetListener dateListener2 = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker datePicker, int year, int month,
				int dayOfMonth) {
			String text = year + "-" + (month + 1) + "-" + dayOfMonth;
			tv_query_endtime.setText(text);
		}
	};

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_data_starttime:
				new DatePickerDialog(DataQueryActivity.this, dateListener1,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.tv_data_endtime:
				new DatePickerDialog(DataQueryActivity.this, dateListener2,
						calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH),
						calendar.get(Calendar.DAY_OF_MONTH)).show();
				break;
			case R.id.btn_data_query:
				DeviceDataDialog.mPage = 0;
				dataListDialog = new DeviceDataDialog(DataQueryActivity.this);
				getDataList();
				break;
			}
		}

	};

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem refreshItem = menu.add(getString(R.string.refresh));
		refreshItem.setIcon(R.drawable.ic_action_refresh);
		refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		refreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				getData(TYPE_GET_NEWLY_DATA);
				return true;
			}
		});

		return true;
	}

	private void displayDataDetail(DeviceData data, int type) {
		if (data.getUId() == null) {
			if (this != null)
				Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
			return;
		}
		if (type == TYPE_GET_NEWLY_DATA) {
			tv_data_title.setText("即时数据");
		} else if (type == TYPE_GET_DATA_DETAIL) {
			tv_data_title.setText("历史数据");
		}
		tv_heart.setText("" + data.getHeartRate());
		if (data.getFlag() == 1) {
			tv_data_state.setText("未审核");
		} else {
			tv_data_state.setText("已审核");
		}
		if (data.getSendMode() == 1) {
			tv_send_mode.setText("自动");
		} else {
			tv_send_mode.setText("手动");
		}
		tv_data_receive_time.setText(data.getReceiveDt());

		double[] values = data.getValue();
		/*
		 * Date[] dates = new Date[values.length]; Calendar cal =
		 * Calendar.getInstance(); cal.setTime(new Date()); for (int i = 0; i <
		 * values.length; i++) { dates[i] = cal.getTime();
		 * cal.add(Calendar.MINUTE, 1); }
		 */
		if (values != null && values.length != 0) {
			int length = values.length;
			double[] x = new double[length];
			if (length == 1) {
				x[0] = values[0];
			} else {
				double start = 0;
				double step = 2000 / (length - 1);
				for (int i = 0; i < length; i++) {
					x[i] = start;
					start += step;
				}
			}
			layout_chart.addView(new TimeChart(this).getChart(x, values));
		}
	}

	public void getDetailofDataItem(String dataUid) {
		this.dataUid = dataUid;
		getData(TYPE_GET_DATA_DETAIL);
	}

	private void displayDataList(List<DeviceData> datas) {
		if (DeviceDataDialog.mPage == 0) {
			dataListDialog.show(datas);
		} else {
			dataListDialog.appendData(datas);
		}
	}

	public void getDataList() {
		getData(TYPE_GET_DATA_LIST);
	}

	private void getData(final int type) {

		String url;
		RequestParams params = new RequestParams();
		switch (type) {
		case TYPE_GET_NEWLY_DATA:
			url = RestClient.URL_GET_NEWLY_DATA;
			params.put("deviceUid", deviceUid);
			break;
		case TYPE_GET_DATA_LIST:
			url = RestClient.URL_GET_DATA_LIST;
			params.put("deviceUid", deviceUid);
			params.put("startDt", tv_query_starttime.getText().toString());
			params.put("endDt", tv_query_endtime.getText().toString());
			params.put("page", "" + DeviceDataDialog.mPage);
			break;
		case TYPE_GET_DATA_DETAIL:
			url = RestClient.URL_GET_DATA_DETAIL;
			params.put("dataUid", dataUid);
			break;
		default:
			url = "";
			break;
		}

		RestClient.get(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				if (type == TYPE_GET_DATA_LIST) {
					progressdialog.show();
				} else {
					progress.setVisibility(View.VISIBLE);
					layout_content.setVisibility(View.GONE);
				}
			}

			@Override
			public void onSuccess(String response) {
				try {
					if (type == TYPE_GET_DATA_LIST) {
						List<DeviceData> datas = JSON.parseArray(response,
								DeviceData.class);
						if (datas != null && datas.size() != 0)
							displayDataList(datas);
						else {
							Toast.makeText(DataQueryActivity.this, "没有数据",
									Toast.LENGTH_SHORT).show();
							dataListDialog.removeFoot();
						}
					} else {
						DeviceData data = JSON.parseObject(response,
								DeviceData.class);
						if (data != null)
							displayDataDetail(data, type);
					}

				} catch (Exception ex) {
					if (type == TYPE_GET_DATA_LIST) {
						dataListDialog.removeFoot();
					}
					try {
						JSONObject json = new JSONObject(response);
						if (json.has("error_msg")) {
							Toast.makeText(DataQueryActivity.this,
									json.getString("error_msg"),
									Toast.LENGTH_SHORT).show();
						}
					} catch (Exception ex1) {
						Toast.makeText(DataQueryActivity.this,
								"error:" + ex1.getMessage(), Toast.LENGTH_SHORT)
								.show();
						ex1.printStackTrace();
					}
				}
			}

			@Override
			public void onFailure(Throwable error) {
				if (type == TYPE_GET_DATA_LIST) {
					if (DeviceDataDialog.mPage > 0)
						DeviceDataDialog.mPage--;
				}
				Toast.makeText(DataQueryActivity.this,
						"error:" + error.getMessage(), Toast.LENGTH_SHORT)
						.show();
			}

			@Override
			public void onFinish() {
				if (type == TYPE_GET_DATA_LIST) {
					progressdialog.dismiss();
				} else {
					progress.setVisibility(View.GONE);
					layout_content.setVisibility(View.VISIBLE);
				}
			}
		});

	}

}
