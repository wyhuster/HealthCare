package dev.hust.leaf.healthcare.activity;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import dev.hust.leaf.healthcare.Constants;
import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.adapter.HomePagerAdapter;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

public class MainActivity extends SherlockFragmentActivity implements
		OnPageChangeListener, TabListener {

	private ViewPager mPager;
	private HomePagerAdapter adapter;
	private SharedPreferences sp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_main);

		// set pager
		mPager = (ViewPager) this.findViewById(R.id.pager);
		adapter = new HomePagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(this);
		mPager.setPageMarginDrawable(R.drawable.grey_border_inset_lr);
		mPager.setPageMargin(this.getResources().getDimensionPixelSize(
				R.dimen.page_margin_width));

		// set actionbar
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.addTab(actionBar.newTab().setText(R.string.user_info)
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText(R.string.device_info)
				.setTabListener(this));

		sp = PreferenceManager.getDefaultSharedPreferences(this);
		int type = sp.getInt(Constants.PREF_USER_TYPE, 1);
		if (type == 1) {
			actionBar.addTab(actionBar.newTab().setText(R.string.monitor)
					.setTabListener(this));
		} else {
			actionBar.addTab(actionBar.newTab()
					.setText(R.string.monitor_object).setTabListener(this));
		}

		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	/*
	 * @Override public boolean onOptionsItemSelected(MenuItem item) {
	 * 
	 * switch (item.getItemId()) { case android.R.id.home: toggle(); return
	 * true; } return super.onOptionsItemSelected(item); }
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem refreshItem = menu.add(getString(R.string.refresh));
		refreshItem.setIcon(R.drawable.ic_action_refresh);
		refreshItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		refreshItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				int index = getSupportActionBar().getSelectedNavigationIndex();
				switch (index) {
				case 0:
					adapter.userFragment.refresh();
					break;
				case 1:
					adapter.deviceFragment.refresh();
					break;
				case 2:
				default:
					adapter.monitorFragment.refresh();
					break;
				}
				return true;
			}
		});

		MenuItem logoutItem = menu.add(getString(R.string.logout));
		// logoutItem.setIcon(R.drawable.ic_action_refresh);
		logoutItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		logoutItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				logout();
				return true;
			}
		});

		return true;
	}

	private void logout() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("退出登录?");
		builder.setTitle("确认");
		builder.setPositiveButton("是",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						sp.edit().clear().commit();
						startActivity(new Intent(MainActivity.this,
								LoginActivity.class));
						MainActivity.this.finish();
					}
				});
		builder.setNegativeButton("否",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		builder.show();
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		getSupportActionBar().setSelectedNavigationItem(position);

	}

}
