package dev.hust.leaf.healthcare.adapter;

import java.util.ArrayList;
import java.util.List;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.model.TotalUser;
import dev.hust.leaf.healthcare.utils.UserDialog;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MonitorAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater mInflater;
	private List<TotalUser> users;

	public MonitorAdapter(List<TotalUser> users, Context context) {
		if (users == null) {
			this.users = new ArrayList<TotalUser>();
		} else {
			this.users = users;
		}
		this.context = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public TotalUser getItem(int position) {
		return users.get(position);
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
			row = mInflater.inflate(R.layout.item_monitor, parent, false);
			holder = new ViewHolder();
			holder.tv_monitor_name = (TextView) row
					.findViewById(R.id.tv_monitor_name);
			holder.tv_monitor_phone = (TextView) row
					.findViewById(R.id.tv_monitor_phone);
			holder.tv_monitor_email = (TextView) row
					.findViewById(R.id.tv_monitor_email);
			holder.btn_more = (TextView) row
					.findViewById(R.id.btn_monitor_more);
			holder.btn_dail = (TextView) row
					.findViewById(R.id.btn_monitor_dail);
			holder.btn_email = (TextView) row
					.findViewById(R.id.btn_monitor_emailto);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final TotalUser user = users.get(position);
		if (user != null && user.getUser() != null) {
			holder.tv_monitor_name.setText(user.getUser().getRealName());
			holder.tv_monitor_phone.setText(user.getUser().getPhone());
			holder.tv_monitor_email.setText(user.getUser().getEmail());

			OnClickListener listener = new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.btn_monitor_more:
						new UserDialog(context).show(user);
						break;
					case R.id.btn_monitor_dail:
						call(user.getUser().getPhone());
						break;
					case R.id.btn_monitor_emailto:
						email(user.getUser().getEmail());
						break;
					}

				}
			};
			holder.btn_more.setOnClickListener(listener);
			holder.btn_dail.setOnClickListener(listener);
			holder.btn_email.setOnClickListener(listener);
		}
		return row;
	}

	private void call(final String phone) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage("∫ÙΩ– " + phone + " ?");
		builder.setTitle("»∑»œ");
		builder.setPositiveButton(" «",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri number = Uri.parse("tel:" + phone);
						Intent intent = new Intent(Intent.ACTION_CALL, number);
						context.startActivity(intent);
						dialog.dismiss();
					}
				});
		builder.setNegativeButton("∑Ò",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.show();
	}

	private void email(String receiver) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("plain/text");
		intent.putExtra(Intent.EXTRA_EMAIL, receiver);
		intent.putExtra(Intent.EXTRA_SUBJECT, "HealthCare");
		// intent.putExtra(Intent.EXTRA_TEXT,"");
		context.startActivity(intent);
	}

	private class ViewHolder {
		TextView tv_monitor_name;
		TextView tv_monitor_phone;
		TextView tv_monitor_email;
		TextView btn_more;
		TextView btn_dail;
		TextView btn_email;
	}

}
