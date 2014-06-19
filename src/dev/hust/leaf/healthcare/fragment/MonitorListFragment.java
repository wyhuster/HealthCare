package dev.hust.leaf.healthcare.fragment;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.app.SherlockListFragment;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.adapter.MonitorAdapter;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.TotalUser;
import dev.hust.leaf.healthcare.model.User;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

public class MonitorListFragment extends SherlockListFragment {
	private List<TotalUser> users;
	private String userId;
	private int type;

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
		setRetainInstance(true);
		// setHasOptionsMenu(true);
		if (init) {
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(getActivity());
			userId = sp.getString(Constants.PREF_USER_UID, "");
			type = sp.getInt(Constants.PREF_USER_TYPE, 1);

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

	private void display() {
		if (users != null && users.size() != 0) {
			MonitorAdapter adapter = new MonitorAdapter(users, getActivity());
			setListAdapter(adapter);
		}
	}

	public void refresh() {
		getData(userId);
	}

	private void getData(String userUid) {
		users = new ArrayList<TotalUser>();
		RequestParams params = new RequestParams();
		params.put("userUid", userUid);
		RestClient.get(RestClient.URL_GET_CONTACT_LIST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (!request_cancle)
							setListShown(false);
					}

					@Override
					public void onSuccess(String response) {
						try {
							if (type != 1) {
								users = JSON.parseArray(response,
										TotalUser.class);
							} else {
								List<User> user = JSON.parseArray(response,
										User.class);
								if (user != null) {
									for (User u : user) {
										TotalUser tu = new TotalUser();
										tu.setUser(u);
										tu.setTerUser(null);
										users.add(tu);
									}
								}
							}
						} catch (Exception ex1) {
							try {
								JSONObject json = new JSONObject(response);
								if (json.has("error_msg")) {
									Toast.makeText(getActivity(),
											json.getString("error_msg"),
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(getActivity(),
											"Êý¾Ý´íÎó:" + json, Toast.LENGTH_SHORT)
											.show();

								}

							} catch (JSONException ex) {
								Toast.makeText(getActivity(),
										"error:" + ex.getMessage(),
										Toast.LENGTH_SHORT).show();
								ex.printStackTrace();

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
							display();
							setListShown(true);
						}
					}
				});

	}
}
