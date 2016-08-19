package com.ostro.myshoppinglist.ui.list;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.clickable_recycler.ClickableRecyclerAdapter;
import com.ostro.myshoppinglist.model.Item;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class ItemAdapter extends ClickableRecyclerAdapter<Item, ItemHolder> {

    public ItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getHolderResources(int viewType) {
        return R.layout.list_item_element;
    }

    @Override
    protected ItemHolder getHolderView(View view, int viewType) {
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = getItem(position);
        if (item != null) {
            if (!TextUtils.isEmpty(item.wording)) {
                holder.tvWordingItem.setText(item.wording);
            }
        }
    }
}
