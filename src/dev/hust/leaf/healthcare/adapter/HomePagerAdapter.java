package dev.hust.leaf.healthcare.adapter;

import dev.hust.leaf.healthcare.fragment.DeviceFragment;
import dev.hust.leaf.healthcare.fragment.MonitorListFragment;
import dev.hust.leaf.healthcare.fragment.UserFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomePagerAdapter extends FragmentPagerAdapter {
	public UserFragment userFragment = new UserFragment();
	public DeviceFragment deviceFragment = new DeviceFragment();
	public MonitorListFragment monitorFragment = new MonitorListFragment();

	public HomePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return userFragment;
		case 1:
			return deviceFragment;
		case 2:
		default:
			return monitorFragment;
		}
	}

	@Override
	public int getCount() {

		return 3;
	}

}
