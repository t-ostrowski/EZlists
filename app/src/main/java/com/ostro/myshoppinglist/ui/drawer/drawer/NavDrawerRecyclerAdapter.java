package com.ostro.myshoppinglist.ui.drawer.drawer;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.clickable_recycler.ClickableHolder;
import com.ostro.myshoppinglist.base.clickable_recycler.ClickableRecyclerAdapter;

import butterknife.BindView;

/**
 * Created by Thomas Ostrowski
 * thomas.o@tymate.com
 * on 12/10/15.
 */
public class NavDrawerRecyclerAdapter extends ClickableRecyclerAdapter<NavigationDrawerItem,
        NavDrawerRecyclerAdapter.NavDrawerHolder> {

    private int mCurrentPositionChecked = -1;

    public NavDrawerRecyclerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getHolderResources(int viewType) {
        return R.layout.list_item_nav_drawer;
    }

    @Override
    protected NavDrawerHolder getHolderView(View view, int viewType) {
        return new NavDrawerHolder(view);
    }

    @Override
    public void onBindViewHolder(NavDrawerHolder holder, int position) {
        NavigationDrawerItem item = getItem(position);
        holder.tvTitle.setText(item.getTitle());
        holder.ivIcon.setImageResource(item.getIcon());
        holder.itemView.setSelected(position == mCurrentPositionChecked);
    }

    public void setItemSelected(int position) {
        mCurrentPositionChecked = position;
        notifyDataSetChanged();
    }

    static class NavDrawerHolder extends ClickableHolder {
        @BindView(R.id.icon)
        ImageView ivIcon;
        @BindView(R.id.title)
        TextView tvTitle;

        public NavDrawerHolder(View view) {
            super(view);
        }
    }
}
