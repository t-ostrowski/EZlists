package com.ostro.myshoppinglist.ui.list;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

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
        return new ItemHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Item item = getItem(position);
        if (item != null) {
            if (item.isValid()) {
                if (!TextUtils.isEmpty(item.getWording())) {
                    holder.tvWordingItem.setText(item.getWording());
                }
                if (item.isChecked()) {
                    holder.btnDeleteItem.setVisibility(View.VISIBLE);
                    holder.tvWordingItem.setText(item.getWording(), TextView.BufferType.SPANNABLE);
                    holder.tvWordingItem.setPaintFlags(holder.tvWordingItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.tvWordingItem.setTypeface(null, Typeface.BOLD_ITALIC);
                    holder.layoutItem.setBackgroundColor(getContext().getResources().getColor(R.color.colorGreyBackground));
                } else {
                    holder.btnDeleteItem.setVisibility(View.GONE);
                    holder.tvWordingItem.setText(item.getWording());
                    holder.tvWordingItem.setPaintFlags(0);
                    holder.tvWordingItem.setTypeface(null, Typeface.NORMAL);
                    holder.layoutItem.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.clickable_highlight));
                }
            }
        }
    }
}
