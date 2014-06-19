package dev.hust.leaf.healthcare.fragment;

import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.adapter.ExceptionAdapter;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.DeviceException;

public class ExceptionFragment extends SherlockListFragment {
	private String deviceUid;
	private int type;
	private ListView list_view;
	private Button foot;
	private List<DeviceException> exceptions;
	private ExceptionAdapter adapter;
	private int mPage;
	private Activity mActivity;
	private ProgressDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		setHasOptionsMenu(true);
		mActivity = getActivity();
		deviceUid = mActivity.getIntent().getStringExtra("deviceUid");

		dialog = new ProgressDialog(getActivity());
		dialog.setMessage("Мгдижа...");

		list_view = this.getListView();

		foot = new Button(getActivity());
		foot.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		foot.setGravity(Gravity.CENTER);
		foot.setText(getActivity().getString(R.string.nextpage));
		foot.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPage++;
				getData(deviceUid, mPage, type);

			}
		});

		// type = 0;
		// alterData(type);

	}

	public void alterData(int type) {
		this.type = type;
		mPage = 0;
		setListAdapter(null);
		if (list_view.getFooterViewsCount() > 0)
			list_view.removeFooterView(foot);
		getData(deviceUid, mPage, type);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		MenuItem refreshItem = menu.add(getString(R.string.refresh));
		refreshItem.setIcon(R.drawable.ic_action_refresh);
		refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		refreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				mPage = 0;
				getData(deviceUid, mPage, type);
				return true;
			}
		});

	}

	private void display(List<DeviceException> exceptions) {

		if (mPage == 0) {
			if (exceptions != null) {
				if (exceptions.size() >= Constants.COUNT_PER_PAGE) {
					if (list_view.getFooterViewsCount() == 0)
						list_view.addFooterView(foot);
				}

				adapter = new ExceptionAdapter(getActivity(), exceptions);
				setListAdapter(adapter);
			}
		} else {
			if (exceptions != null) {
				if (adapter != null) {
					adapter.appendData(exceptions);
				}
				if (exceptions.size() < Constants.COUNT_PER_PAGE) {
					if (list_view.getFooterViewsCount() >= 0)
						list_view.removeFooterView(foot);
				}

			} else {
				if (list_view.getFooterViewsCount() > 0)
					list_view.removeFooterView(foot);
			}
		}
	}

	private void getData(String deviceUid, int page, int type) {
		RequestParams params = new RequestParams();
		params.put("DeviceUid", deviceUid);
		params.put("page", "" + page);
		params.put("ExceptionGroup", "" + type);
		RestClient.get(RestClient.URL_GET_EXCEPTION_LIST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (mPage == 0)
							setListShown(false);
						else
							dialog.show();
					}

					@Override
					public void onSuccess(String response) {
						// Log.i("wy", response);
						try {
							exceptions = JSON.parseArray(response,
									DeviceException.class);

						} catch (Exception ex) {
							exceptions = null;
							try {
								JSONObject json = new JSONObject(response);
								if (json.has("error_msg")) {
									Toast.makeText(getActivity(),
											json.getString("error_msg"),
											Toast.LENGTH_SHORT).show();
								}
							} catch (Exception ex1) {
								Toast.makeText(getActivity(),
										"error:" + ex1.getMessage(),
										Toast.LENGTH_SHORT).show();
								ex1.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(Throwable error) {
						exceptions = null;
						Toast.makeText(getActivity(),
								"error:" + error.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						display(exceptions);
						if (mPage == 0)
							setListShown(true);
						else
							dialog.dismiss();
					}
				});

	}

}
