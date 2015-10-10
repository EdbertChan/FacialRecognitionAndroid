package com.edbert.library.navigationdrawer;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.edbert.library.navigationdrawer.NavDrawerItemInterface;

public class NavDrawerItem implements NavDrawerItemInterface {
    protected ArrayList<Type> TYPE = new ArrayList<Type>();
    private String title;
    private int icon;
    private int count = 0;
    // boolean to set visiblity of the counter
    private boolean isCounterVisible = false;
public static final int NO_ICON = 0;
    private String actionBarTitle;
    private NavDrawerActionInterface action;
    private Fragment f;

    public String getActionBarTitle(){return actionBarTitle;};
    @Override
    public Fragment getFragment() {
        if (f == null) {
            throw new RuntimeException("You must call setFragment()");
        }
        return f;
    }

    @Override
    public Type getNavDrawerType() {
        return TYPE.get(0);
    }

    public ArrayList<Type> getAllTypes() {
        return TYPE;
    }


    @Override
    public void doAction(Context c) {
        if (action == null) {

            throw new RuntimeException("You must call setAction()");
        }
        action.doAction(c);
        return;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public String actionBarTitle() {
        return null;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
this.title = title;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public void setCount(int count) {
this.count = count;
    }

    @Override
    public int getIcon() {
        return icon;
    }

    @Override
    public void setIcon(int icon) {
this.icon = icon;
    }

    ///



        private NavDrawerItem(NavDrawerItemBuilder builder) {
            TYPE = builder.TYPE;
            title = builder.title;
            icon = builder.icon;
            count = builder.count;
            isCounterVisible = builder.isCounterVisible;
            actionBarTitle = builder.actionBarTitle;
            action = builder.action;
            f = builder.f;
        }


        public static class NavDrawerItemBuilder {
            protected ArrayList<Type> TYPE = new ArrayList<Type>();
            private String title;
            private int icon;
            private int count = 0;
            // boolean to set visiblity of the counter
            private boolean isCounterVisible = false;

            private String actionBarTitle;
            private NavDrawerActionInterface action;
            private Fragment f;


            public NavDrawerItemBuilder(String title, int icon) {
                this.title = title;
                this.icon = icon;
            }

            public NavDrawerItemBuilder type(Type type) {
                TYPE.add(type);
                return this;
            }

            public NavDrawerItemBuilder count(int count) {
                this.count = count;
                return this;
            }

            public NavDrawerItemBuilder action(NavDrawerActionInterface action) {
                this.action = action;
                return this;
            }

            public NavDrawerItemBuilder fragment(Fragment f) {
                this.f = f;
                return this;
            }

            public NavDrawerItem build() {
                return new NavDrawerItem(this);
            }

        }

}