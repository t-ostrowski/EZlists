package com.ostro.ezlists.ui.list;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ostro.ezlists.R;
import com.ostro.ezlists.base.clickable_recycler.ClickableHolder;
import com.ostro.ezlists.model.List;
import com.ostro.ezlists.ui.details.DetailsListActivity;
import com.ostro.ezlists.ui.list.dialog.DialogCloseListener;
import com.ostro.ezlists.ui.list.dialog.EditListDialog;
import com.ostro.ezlists.ui.list.dialog.EditListDialogOpenListener;

import butterknife.BindView;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class ListHolder extends ClickableHolder {

    @BindView(R.id.tv_wording_list)
    TextView tvNameList;
    @BindView(R.id.layout_list)
    RelativeLayout layoutList;
    @BindView(R.id.btn_edit_list)
    ImageView btnEditList;

    private ListAdapter adapter;
    private Activity activity;

    public ListHolder(View view, ListAdapter adapter, Activity activity) {
        super(view);
        this.adapter = adapter;
        this.activity = activity;
    }

    @Nullable
    protected View getClickableView() {
        return itemView;
    }

    @OnClick(R.id.layout_list)
    public void onListClick() {
        if (adapter != null) {
            List list = adapter.getItem(getAdapterPosition());
            if (list != null) {
                if (list.getId() > 0L) {
                    Intent intent = new Intent(adapter.getContext(), DetailsListActivity.class);
                    intent.putExtra("LIST_ID", list.getId());
                    adapter.getContext().startActivity(intent);
                }
            }
        }
    }

    @OnClick(R.id.btn_edit_list)
    public void onEditList() {
        if (adapter != null) {
            List list = adapter.getItem(getAdapterPosition());
            if (list != null) {
                if (list.getId() > 0L) {
                    Activity activity = adapter.getActivity();
                    if (activity instanceof EditListDialogOpenListener) {
                        ((EditListDialogOpenListener) activity).handleOpenEditDialog(list.getId());
                    }
                }
            }
        }
    }
}
