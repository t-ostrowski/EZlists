package com.ostro.myshoppinglist.ui.drawer.drawer;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.clickable_recycler.OnItemClickListener;
import com.ostro.myshoppinglist.ui.drawer.NavigationDrawerActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Thomas Ostrowski
 * thomas.o@tymate.com
 * on 12/10/15.
 */
public class NavigationDrawerView extends RelativeLayout {

    private static final String TAG = "NavigationDrawerView";

    @BindView(R.id.navigation_drawer_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_nav_drawer_header)
    ImageView mIvNavDrawerHeader;

    private NavigationDrawerActivity mActivity;
    private View mDrawerContainer;
    private DrawerLayout mDrawerLayout;
    private NavDrawerRecyclerAdapter mAdapter;
    private Resources mResources;

    public NavigationDrawerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mResources = context.getResources();
        mActivity = (NavigationDrawerActivity) context;
        initAdapter();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this, this);
//        Glide.with(getContext()).load(R.drawable.logo).fitCenter().into(mIvNavDrawerHeader);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(mOnItemClickListener);
    }

    private void initAdapter() {

        String[] primaryTitles = mResources.getStringArray(R.array.item_nav_drawer_title);
        TypedArray primaryIcons = mResources.obtainTypedArray(R.array.item_nav_drawer_icon);

        List<NavigationDrawerItem> navigationDrawerItems = new ArrayList<>();
        navigationDrawerItems.addAll(createNavigationsItems(primaryTitles, primaryIcons, NavigationDrawerItem.Type.PRIMARY));
        mAdapter = new NavDrawerRecyclerAdapter(getContext());
        mAdapter.setItems(navigationDrawerItems);
        mAdapter.notifyDataSetChanged();
        primaryIcons.recycle();
    }

    private List<NavigationDrawerItem> createNavigationsItems(String[] titles, TypedArray icons, NavigationDrawerItem.Type type) {
        int i = 0;
        List<NavigationDrawerItem> items = new ArrayList<>();
        for (String title : titles) {
            items.add(new NavigationDrawerItem(title, icons.getResourceId(i, -1), type));
            i++;
        }
        return items;
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            closeDrawer();
            mActivity.loadFragment(position);
        }
    };

    public void setItemChecked(int position) {
        if (mAdapter != null) {
            mAdapter.setItemSelected(position);
        }
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mDrawerContainer = mActivity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
    }

    private void closeDrawer() {
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawer(mDrawerContainer);
        }
    }

    public void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }
}
