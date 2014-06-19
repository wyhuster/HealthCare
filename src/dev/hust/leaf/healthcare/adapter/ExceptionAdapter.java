package dev.hust.leaf.healthcare.adapter;

import java.util.ArrayList;
import java.util.List;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.model.DeviceException;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExceptionAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<DeviceException> exceptions;

	public ExceptionAdapter(Context context, List<DeviceException> exceptions) {
		if (exceptions == null) {
			this.exceptions = new ArrayList<DeviceException>();
		} else {
			this.exceptions = exceptions;
		}

		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void appendData(List<DeviceException> exceptions) {
		if (exceptions != null) {
			for (DeviceException e : exceptions) {
				this.exceptions.add(e);
			}
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return exceptions.size();
	}

	@Override
	public DeviceException getItem(int position) {
		return exceptions.get(position);
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
			row = mInflater.inflate(R.layout.item_exception, parent, false);
			holder = new ViewHolder();
			holder.tv_type = (TextView) row
					.findViewById(R.id.tv_exception_type);
			holder.tv_solved = (TextView) row
					.findViewById(R.id.tv_exception_solved);
			holder.tv_starttime = (TextView) row
					.findViewById(R.id.tv_exception_starttime);
			holder.tv_endtime = (TextView) row
					.findViewById(R.id.tv_exception_endtime);
			holder.tv_message = (TextView) row
					.findViewById(R.id.tv_exception_desc);
			
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		DeviceException exception = exceptions.get(position);
		holder.tv_type.setText(exception.getTypeString());
		holder.tv_solved.setText(exception.getStatusString());
		holder.tv_starttime.setText(exception.getStartTime());
		holder.tv_endtime.setText(exception.getEndTime());
		holder.tv_message.setText(exception.getMessage());

		return row;
	}

	private class ViewHolder {
		TextView tv_type;
		TextView tv_solved;
		TextView tv_starttime;
		TextView tv_endtime;
		TextView tv_message;
	}
}
