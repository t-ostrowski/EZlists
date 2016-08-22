package com.ostro.myshoppinglist.ui.list;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.clickable_recycler.ClickableHolder;
import com.ostro.myshoppinglist.model.Item;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class ItemHolder extends ClickableHolder {

    @BindView(R.id.layout_item)
    RelativeLayout layoutItem;
    @BindView(R.id.tv_wording_item)
    TextView tvWordingItem;
    @BindView(R.id.btn_delete_item)
    ImageView btnDeleteItem;

    private ItemAdapter itemAdapter;
    private Realm realm = Realm.getDefaultInstance();

    public ItemHolder(View view, ItemAdapter adapter) {
        super(view);
        this.itemAdapter = adapter;
    }

    @Nullable
    protected View getClickableView() {
        return itemView;
    }

    @OnClick(R.id.layout_item)
    public void onClick() {
        if (itemAdapter != null) {
            Item item = itemAdapter.getItem(getAdapterPosition());
            changeState(item);
            itemAdapter.notifyDataSetChanged();
        }
    }

    private void changeState(Item item) {
        if (item.isChecked()) {
            realm.beginTransaction();
            item.setChecked(false);
            realm.commitTransaction();
        } else {
            realm.beginTransaction();
            item.setChecked(true);
            realm.commitTransaction();
        }
    }

    @OnClick(R.id.btn_delete_item)
    public void onDeleteItem() {
        if (itemAdapter != null) {
            Item item = itemAdapter.getItem(getAdapterPosition());
            realm.beginTransaction();
            RealmResults<Item> result = realm.where(Item.class).equalTo("id", item.getId()).findAll();
            result.deleteAllFromRealm();
            realm.commitTransaction();
            itemAdapter.notifyDataSetChanged();
        }
    }
}
