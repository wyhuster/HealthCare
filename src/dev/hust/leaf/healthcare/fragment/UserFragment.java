package dev.hust.leaf.healthcare.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.http.RestClient;
import dev.hust.leaf.healthcare.model.TerUser;
import dev.hust.leaf.healthcare.model.TotalUser;
import dev.hust.leaf.healthcare.model.User;

public class UserFragment extends SherlockFragment {

	private SharedPreferences sp;
	private ProgressBar progressbar;
	private View layout_content;
	private TextView tv_username;
	private TextView tv_realname;
	private TextView tv_sex;
	private TextView tv_email;
	private TextView tv_phone;
	private TextView tv_type;
	private TextView tv_birthday;
	private TextView tv_identifyID;
	private TextView tv_address;
	private TextView tv_career;
	private TextView tv_comment;
	private ImageView img_avatar;
	private Bitmap bm;
	private String userId;

	private TotalUser totaluser;
	private boolean init = true;
	private boolean request_cancle;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater
				.inflate(R.layout.layout_user_fragment, container, false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		// setHasOptionsMenu(true);

		Activity a = getActivity();
		progressbar = (ProgressBar) a.findViewById(R.id.progress_bar);

		layout_content = a.findViewById(R.id.layout_content);
		tv_username = (TextView) a.findViewById(R.id.tv_username);
		tv_realname = (TextView) a.findViewById(R.id.tv_realname);
		tv_sex = (TextView) a.findViewById(R.id.tv_sex);
		tv_email = (TextView) a.findViewById(R.id.tv_email);
		tv_phone = (TextView) a.findViewById(R.id.tv_phone);
		tv_type = (TextView) a.findViewById(R.id.tv_type);
		tv_birthday = (TextView) a.findViewById(R.id.tv_birthday);
		tv_identifyID = (TextView) a.findViewById(R.id.tv_identifyID);
		tv_address = (TextView) a.findViewById(R.id.tv_address);
		tv_career = (TextView) a.findViewById(R.id.tv_career);
		tv_comment = (TextView) a.findViewById(R.id.tv_comment);
		img_avatar = (ImageView) a.findViewById(R.id.img_avatar);

		sp = PreferenceManager.getDefaultSharedPreferences(a);
		userId = sp.getString(Constants.PREF_USER_UID, "");
		int userType = sp.getInt(Constants.PREF_USER_TYPE, 1);
		if (userType != 1) {
			a.findViewById(R.id.row_identifyID).setVisibility(View.GONE);
			a.findViewById(R.id.row_address).setVisibility(View.GONE);
			a.findViewById(R.id.row_birthday).setVisibility(View.GONE);
			a.findViewById(R.id.row_career).setVisibility(View.GONE);
			a.findViewById(R.id.row_comment).setVisibility(View.GONE);
		}

		if (init) {
			// getData(userId);
			getData();
			init = false;
		} else {
			display();
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

	@Override
	public void onDestroy() {
		if (bm != null) {
			bm.recycle();
			bm = null;
		}
		super.onDestroy();
	}

	private void display() {
		if (totaluser != null) {
			User user = totaluser.getUser();
			TerUser teruser = totaluser.getTerUser();

			if (user != null) {
				tv_username.setText(user.getUserName());
				tv_realname.setText(user.getRealName());
				tv_sex.setText(user.getSexString());
				tv_email.setText(user.getEmail());
				tv_phone.setText(user.getPhone());
				tv_type.setText(user.getUserTypeString());
				byte[] b = user.getPhoto();
				if (b != null) {
					bm = BitmapFactory.decodeByteArray(b, 0, b.length);
					img_avatar.setImageBitmap(bm);
				}
			}
			if (teruser != null) {
				tv_birthday.setText(teruser.getBirthDt().trim().split(" ")[0]);
				tv_identifyID.setText(teruser.getIdentityCode());
				tv_address.setText(teruser.getAddress());
				tv_career.setText(teruser.getOccupation());
				tv_comment.setText(teruser.getMemo());
			}
		}
	}

	public void refresh() {
		getData(userId);
	}

	private void getData() {
		String user_json = sp.getString(Constants.PREF_USER, "");
		if (user_json == null || user_json.equals("")) {
			// get from server
			getData(userId);
		} else {
			try {
				// get from local
				totaluser = JSON.parseObject(user_json, TotalUser.class);
				display();
			} catch (Exception ex) {
				getData(userId);
			}
		}
	}

	private void getData(String userUid) {
		RequestParams params = new RequestParams();
		params.put("userUid", userUid);
		RestClient.get(RestClient.URL_GET_USER_INFO, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						if (!request_cancle) {
							progressbar.setVisibility(View.VISIBLE);
							layout_content.setVisibility(View.GONE);
						}
					}

					@Override
					public void onSuccess(String response) {
						try {
							JSONObject json = new JSONObject(response);
							if (json.has("error_msg")) {
								Toast.makeText(getActivity(),
										json.getString("error_msg"),
										Toast.LENGTH_SHORT).show();
							} else {
								if (json.has("teruser") || json.has("UserUId")) {
									User user;
									TerUser teruser;
									// user type 1
									if (json.has("teruser")) {
										user = JSON.parseObject(json
												.getJSONObject("user")
												.toString(), User.class);
										teruser = JSON.parseObject(json
												.getJSONObject("teruser")
												.toString(), TerUser.class);

									} else {
										user = JSON.parseObject(response,
												User.class);
										teruser = null;
									}

									totaluser = new TotalUser();
									totaluser.setTerUser(teruser);
									totaluser.setUser(user);
									String json_user = JSON
											.toJSONString(totaluser);
									sp.edit()
											.putString(Constants.PREF_USER,
													json_user).commit();

								} else {
									Toast.makeText(getActivity(),
											"数据错误:" + json, Toast.LENGTH_SHORT)
											.show();
								}
							}

						} catch (JSONException ex) {
							Toast.makeText(getActivity(),
									"error:" + ex.getMessage(),
									Toast.LENGTH_SHORT).show();
							ex.printStackTrace();

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
							// 展示数据
							display();
							progressbar.setVisibility(View.GONE);
							layout_content.setVisibility(View.VISIBLE);
						}
					}
				});

	}
}
