package com.edbert.library.navigationdrawer;

/**
 * We implement this to the interface because it allows us to set up the
 * core organization differently. The functional components will be the same.
 * All nav drawer entries will have
 * 1) An activity/fragment
 * 2) A label
 * 3) A count (for badge)
 * 4) An icon
 */
import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public interface NavDrawerItemInterface {
	public enum Type {
	SECTION_HEADER_TYPE, FRAGMENT_TYPE, ACTION_TYPE, NOTHING_TYPE, PROFILE_TYPE
	}

	public Type getNavDrawerType();

	// activity and fragments
	public void doAction(Context c);

	public Fragment getFragment();

	public boolean updateActionBarTitle();

	public String actionBarTitle();

	// Title
	public String getTitle();

	public void setTitle(String title);

	public int getCount();

	public void setCount(int count);

	// TODO: need to set icon but for now we assume as a resource. In the
	// future, we need to download the picture
	// and save it (eg: in the case it is a profile image that we download.
	
	//bitmap it?
	public int getIcon();

	public void setIcon(int icon);

}