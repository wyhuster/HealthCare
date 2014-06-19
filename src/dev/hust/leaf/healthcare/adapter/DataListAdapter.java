package dev.hust.leaf.healthcare.adapter;

import java.util.ArrayList;
import java.util.List;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.model.Device;
import dev.hust.leaf.healthcare.model.DeviceData;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataListAdapter extends BaseAdapter {

	private List<DeviceData> datas;
	private Context context;
	private LayoutInflater mInflater;

	public DataListAdapter(List<DeviceData> datas, Context context) {
		if (datas == null) {
			this.datas = new ArrayList<DeviceData>();
		} else {
			this.datas = datas;
		}
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void appendData(List<DeviceData> adds) {
		if (this.datas != null) {
			for (DeviceData d : adds) {
				datas.add(d);
			}
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public DeviceData getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder;
		if (row == null) {
			row = mInflater.inflate(R.layout.item_data_list, parent, false);
			holder = new ViewHolder();
			holder.tv_time = (TextView) row
					.findViewById(R.id.tv_data_receive_time);
			holder.tv_value = (TextView) row.findViewById(R.id.tv_heart_rate);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		holder.tv_time.setText(datas.get(position).getReceiveDt());
		holder.tv_value.setText("" + datas.get(position).getHeartRate());
		return row;
	}

	private class ViewHolder {
		TextView tv_time;
		TextView tv_value;
	}

}
