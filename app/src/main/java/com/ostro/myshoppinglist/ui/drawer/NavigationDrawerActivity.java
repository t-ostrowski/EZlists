package com.ostro.myshoppinglist.ui.drawer;

import android.animation.ValueAnimator;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.BaseActivity;
import com.ostro.myshoppinglist.base.ToolbarActivity;
import com.ostro.myshoppinglist.ui.drawer.drawer.NavigationDrawerView;

import butterknife.BindView;

/**
 * Created by Thomas Ostrowski
 * thomas.o@tymate.com
 * on 12/10/15.
 */
public abstract class NavigationDrawerActivity extends ToolbarActivity {

    protected enum ActionDrawableState {
        BURGER, ARROW
    }

    private static final String CURRENT_POSITION = "current_position";
    private static final String IS_DRAWER_LOCKED = "is_drawer_locked";

    @BindView(R.id.drawer_layout)
    protected DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation_drawer)
    protected NavigationDrawerView mNavigationDrawerView;

    protected ActionBarDrawerToggle mDrawerToggle;
    private boolean isDrawerLocked = false;
    private int mCurrentPosition;

    @Override
    public int getLayoutResources() {
        return R.layout.activity_nav_drawer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDrawer();
    }

    protected void toggleActionBarIcon(ActionDrawableState state, final ActionBarDrawerToggle toggle, boolean animate) {
        if (animate) {
            float start = state == ActionDrawableState.BURGER ? 0f : 1.0f;
            float end = Math.abs(start - 1);
            ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
            offsetAnimator.setDuration(300);
            offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = (Float) animation.getAnimatedValue();
                    toggle.onDrawerSlide(null, offset);
                }
            });
            offsetAnimator.start();
        } else {
            if (state == ActionDrawableState.BURGER) {
                toggle.onDrawerClosed(null);
            } else {
                toggle.onDrawerOpened(null);
            }
        }
    }

    protected void initDrawer() {
        if (mToolbar == null) {
            throw new RuntimeException("must provide toolbar");
        }
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mToolbar.setNavigationOnClickListener(mToolbarNavigationListener);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationDrawerView.setUp(R.id.navigation_drawer, mDrawerLayout);
    }

    public void onNavigationClicked() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
    }

    public void lockDrawer() {
        toggleActionBarIcon(ActionDrawableState.BURGER, mDrawerToggle, true);
        mNavigationDrawerView.lockDrawer();
        isDrawerLocked = true;
    }

    public void unlockDrawer(boolean animate) {
        toggleActionBarIcon(ActionDrawableState.ARROW, mDrawerToggle, true);
        mNavigationDrawerView.unlockDrawer();
        isDrawerLocked = false;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
        if (savedInstanceState == null) {
            loadFragment(getFragmentBasePosition());
        } else {
            mNavigationDrawerView.setItemChecked(mCurrentPosition);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_DRAWER_LOCKED, isDrawerLocked);
        outState.putInt(CURRENT_POSITION, mCurrentPosition);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPosition = savedInstanceState.getInt(CURRENT_POSITION);
        isDrawerLocked = savedInstanceState.getBoolean(IS_DRAWER_LOCKED);
        if (isDrawerLocked) {
            lockDrawer();
        }
    }

    View.OnClickListener mToolbarNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hideSoftKeyboard();
            if (!isDrawerLocked) {
                onNavigationClicked();
            } else {
                onBackPressed();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return false;
    }

    protected abstract Fragment getFragment(int position);

    protected abstract String getFragmentTag(int position);

    protected abstract int getFragmentBasePosition();

    protected int getFragmentContainer() {
        return R.id.main_fragment_container;
    }

    public void loadFragment(int position) {
        hideSoftKeyboard();
        Fragment fragment = getFragment(position);
        if (fragment == null) {
            return;
        }
        if (mNavigationDrawerView != null) {
            mNavigationDrawerView.setItemChecked(position);
        }
        mCurrentPosition = position;
        super.replaceFragment(getFragmentContainer(), getFragmentTag(position), fragment, false,
                R.anim.scale_in_main_frags,
                R.anim.scale_out_main_frags,
                0, 0);
        if (isDrawerLocked) {
            unlockDrawer(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        if (popStack()) {
            return;
        }
        if (mCurrentPosition != getFragmentBasePosition()) {
            loadFragment(getFragmentBasePosition());
            return;
        }
        super.onBackPressed();
    }


    public void setTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    private boolean popStack() {
        int popStackCount = tryPopStack();
        if (popStackCount >= 1) {
            if (popStackCount == 1) {
                unlockDrawer(true);
            }
            return true;
        }
        return false;
    }
}
