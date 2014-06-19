package dev.hust.leaf.healthcare.activity;

import org.json.JSONException;
import org.json.JSONObject;

import dev.hust.leaf.healthcare.http.RestClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private SharedPreferences sp;
	private ProgressDialog dialog;
	private EditText et_username;
	private EditText et_passwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		sp = PreferenceManager.getDefaultSharedPreferences(this);
		if (sp.contains(Constants.PREF_USER_UID)) {
			startActivity(new Intent(this, MainActivity.class));
			this.finish();
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		dialog = new ProgressDialog(this);
		dialog.setMessage("登录中...");
		dialog.setCancelable(false);

		et_username = (EditText) findViewById(R.id.id_accout);
		et_passwd = (EditText) findViewById(R.id.id_passwd);
		findViewById(R.id.btn_login).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String username = et_username.getText().toString();
				String passwd = et_passwd.getText().toString();
				if (username == null || username.equals("")) {
					Toast.makeText(LoginActivity.this, "账号不能为空!",
							Toast.LENGTH_SHORT).show();
				} else if (passwd == null || passwd.equals("")) {
					Toast.makeText(LoginActivity.this, "密码不能为空!",
							Toast.LENGTH_SHORT).show();
				} else {
					login(username, passwd);
				}
			}

		});
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private void login(String user, String passwd) {
		RequestParams params = new RequestParams();
		params.put("username", user);
		params.put("password", passwd);
		RestClient.get(RestClient.URL_LOGIN, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onStart() {
						dialog.show();
					}

					@Override
					public void onSuccess(String response) {
						try {
							JSONObject json = new JSONObject(response);
							if (json.has("error_msg")) {
								Toast.makeText(LoginActivity.this,
										json.getString("error_msg"),
										Toast.LENGTH_SHORT).show();
							} else {
								if (json.has("UserUId")) {
									Editor editor = sp.edit();
									editor.putString(Constants.PREF_USER_UID,
											json.getString("UserUId"));
									editor.putInt(Constants.PREF_USER_TYPE,
											json.getInt("UserType"));
									editor.commit();

									Toast.makeText(LoginActivity.this, "登录成功",
											Toast.LENGTH_SHORT).show();
									startActivity(new Intent(
											LoginActivity.this,
											MainActivity.class));
									LoginActivity.this.finish();
								} else {
									Toast.makeText(LoginActivity.this,
											"数据错误:" + json, Toast.LENGTH_SHORT)
											.show();
								}
							}

						} catch (JSONException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();

						}

					}

					@Override
					public void onFailure(Throwable error) {
						Toast.makeText(LoginActivity.this,
								"error:" + error.getMessage(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFinish() {
						dialog.dismiss();
					}
				});
	}
}
