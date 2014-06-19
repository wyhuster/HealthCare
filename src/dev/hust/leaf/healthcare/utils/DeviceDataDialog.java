package dev.hust.leaf.healthcare.utils;

import java.util.List;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.activity.DataQueryActivity;
import dev.hust.leaf.healthcare.adapter.DataListAdapter;
import dev.hust.leaf.healthcare.model.DeviceData;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DeviceDataDialog {

	private Context context;
	public static int mPage;
	private DataQueryActivity mActivity;
	private ListView list;
	private DataListAdapter dataListAdapter;
	private Button foot;

	public DeviceDataDialog(Context context) {
		this.context = context;
		mActivity = (DataQueryActivity) context;
	}

	public void show(final List<DeviceData> datas) {
		LayoutInflater inflaterDl = LayoutInflater.from(context);
		View layout = inflaterDl.inflate(R.layout.layout_list_dialog, null);

		final Dialog dialog = new Dialog(context);

		dialog.setTitle("历史数据");
		dialog.setContentView(layout);

		list = (ListView) layout.findViewById(R.id.list_device_data);

		if (datas.size() >= Constants.COUNT_PER_PAGE) {
			foot = new Button(context);
			foot.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			foot.setGravity(Gravity.CENTER);
			foot.setText(context.getString(R.string.nextpage));
			foot.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mPage++;
					mActivity.getDataList();

				}
			});
			list.addFooterView(foot);
		}

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String uid = datas.get(position).getUId();
				mActivity.getDetailofDataItem(uid);
				dialog.dismiss();
			}

		});

		dataListAdapter = new DataListAdapter(datas, context);
		list.setAdapter(dataListAdapter);

		dialog.show();

	}

	public void appendData(List<DeviceData> datas) {
		if (datas.size() < Constants.COUNT_PER_PAGE) {
			removeFoot();
		}
		dataListAdapter.appendData(datas);
	}

	public void removeFoot() {
		if (foot != null)
			list.removeFooterView(foot);
	}

}
