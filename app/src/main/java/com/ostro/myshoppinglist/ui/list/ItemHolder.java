package com.ostro.myshoppinglist.ui.list;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.clickable_recycler.ClickableHolder;

import butterknife.BindView;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class ItemHolder extends ClickableHolder {

    @BindView(R.id.tv_wording_item)
    TextView tvWordingItem;

    public ItemHolder(View view) {
        super(view);
    }

    @Nullable
    protected View getClickableView() {
        return itemView;
    }
}
