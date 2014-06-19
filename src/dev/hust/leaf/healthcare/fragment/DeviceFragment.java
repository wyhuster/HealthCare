package dev.hust.leaf.healthcare.fragment;

import java.util.List;

import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.adapter.DeviceAdapter;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.Device;

public class DeviceFragment extends SherlockListFragment {
	private List<Device> devices;
	private String userId;

	private boolean init = true;
	private boolean request_cancle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// setHasOptionsMenu(true);
		setRetainInstance(true);

		if (init) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			userId = sp.getString(Constants.PREF_USER_UID, "");

			getData(userId);
			init = false;
		}

	}

	@Override
	public void onStart() {
		request_cancle = false;
		super.onStart();
	}

	@Override
	public void onStop() {
		request_cancle = true;
		super.onStop();
	}

	/*
	 * @Override public void onCreateOptionsMenu(Menu menu, MenuInflater
	 * inflater) { MenuItem refreshItem = menu.add(getString(R.string.refresh));
	 * refreshItem.setIcon(R.drawable.ic_action_refresh);
	 * refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	 * refreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
	 * 
	 * @Override public boolean onMenuItemClick(MenuItem item) {
	 * 
	 * getData(userId); return true; } });
	 * 
	 * }
	 */

	private void display(List<Device> devices) {
		setListAdapter(new DeviceAdapter(devices, getActivity()));
	}

	public void refresh() {
		getData(userId);
	}

	private void getData(String userId) {
		RequestParams params = new RequestParams();
		params.put("userUid", userId);
		RestClient.get(RestClient.URL_GET_DEVICE_LIST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (!request_cancle)
							setListShown(false);
					}

					@Override
					public void onSuccess(String response) {
						// Log.i("wy", response);
						try {
							devices = JSON.parseArray(response, Device.class);

						} catch (Exception ex) {
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
						Toast.makeText(getActivity(),
								"error:" + error.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						if (!request_cancle) {
							display(devices);
							setListShown(true);
						}
					}
				});

	}

}
