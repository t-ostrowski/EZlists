package com.ostro.ezlists.ui.list;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.ostro.ezlists.R;
import com.ostro.ezlists.base.clickable_recycler.ClickableRecyclerAdapter;
import com.ostro.ezlists.model.List;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class ListAdapter extends ClickableRecyclerAdapter<List, ListHolder> {

    private Activity activity;

    public ListAdapter(Context context, Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected int getHolderResources(int viewType) {
        return R.layout.list_item_list;
    }

    @Override
    protected ListHolder getHolderView(View view, int viewType) {
        return new ListHolder(view, this, activity);
    }

    @Override
    public void onBindViewHolder(ListHolder holder, int position) {
        List list = getItem(position);
        if (list != null) {
            if (list.isValid()) {
                if (!TextUtils.isEmpty(list.getName())) {
                    holder.tvNameList.setText(list.getName());
                }
            }
        }
    }

    public Activity getActivity() {
        return this.activity;
    }
}
