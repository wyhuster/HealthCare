package dev.hust.leaf.healthcare.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.OnNavigationListener;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;

import dev.hust.leaf.healthcare.R;
import dev.hust.leaf.healthcare.fragment.ExceptionFragment;

public class ExceptionActivity extends SherlockFragmentActivity implements
		OnNavigationListener {

	private ExceptionFragment mFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_exception);

		// setProgressBarIndeterminateVisibility(true);
		String code = getIntent().getStringExtra("deviceCode");
		setTitle("…Ë±∏" + code);

		ActionBar mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mFragment = (ExceptionFragment) this.getSupportFragmentManager()
				.findFragmentById(android.R.id.list);

		SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this,
				R.array.exception_type, R.layout.item_text);

		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		mActionBar.setListNavigationCallbacks(mSpinnerAdapter, this);
		mActionBar.setSelectedNavigationItem(0);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		mFragment.alterData(itemPosition);
		return true;
	}

}
