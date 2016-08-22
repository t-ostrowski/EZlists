package com.ostro.myshoppinglist.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.BaseFragment;
import com.ostro.myshoppinglist.model.Item;
import com.ostro.myshoppinglist.ui.widget.CustomAlertDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 19/08/2016.
 */

public class ListFragment extends BaseFragment {

    @BindView(R.id.rv_item)
    RecyclerView rvItems;
    @BindView(R.id.tv_empty_state)
    TextView tvEmptyState;
    @BindView(R.id.et_new_item)
    EditText etNewItem;
    @BindView(R.id.btn_add_item)
    ImageView btnAddItem;

    private ItemAdapter adapter;
    private Realm realm;
    private boolean allItemsSelected = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.list_your_list));
        setHasOptionsMenu(true);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        realm.close();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        realm.close();
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRecycler(getContext());

        etNewItem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(etNewItem.getText().toString())) {
                        Item itemToAdd = new Item(etNewItem.getText().toString().trim());
                        addItemToList(itemToAdd);
                    }
                    handled = true;
                }
                return handled;
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_select_all:
                checkAllItems();
                return true;
            case R.id.action_delete_selection:
                deleteAllSelectedItems();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        displayEmptyStateIfEmpty(adapter);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public int getLayoutResources() {
        return R.layout.fragment_list;
    }

    @OnClick(R.id.btn_add_item)
    public void onAddItemClick() {
        if (!TextUtils.isEmpty(etNewItem.getText().toString())) {
            Item itemToAdd = new Item(etNewItem.getText().toString().trim());
            addItemToList(itemToAdd);
        }
    }

    private void initRecycler(Context context) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new ItemAdapter(context);

        rvItems.setLayoutManager(layoutManager);
        rvItems.setAdapter(adapter);
    }

    private void displayEmptyStateIfEmpty(ItemAdapter adapter) {
        if (adapter.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            etNewItem.setText("");
        }
    }

    private void loadData() {
        RealmResults<Item> items = realm.where(Item.class).findAll();
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
        displayEmptyStateIfEmpty(adapter);
    }

    private void addItemToList(Item item) {
        realm.beginTransaction();
        item.setId(getNextKey());
        realm.copyToRealm(item);
        realm.commitTransaction();

        loadData();
    }

    private void checkAllItems() {
        realm.beginTransaction();
        List<Item> currentItems = realm.where(Item.class).findAll();
        for (Item item : currentItems) {
            item.setChecked(!allItemsSelected);
        }
        realm.commitTransaction();

        if (allItemsSelected) {
            allItemsSelected = false;
        } else {
            allItemsSelected = true;
        }

        loadData();
    }

    private void deleteAllSelectedItems() {
        realm.beginTransaction();
        RealmResults<Item> selectedItems = realm.where(Item.class).equalTo("checked", true).findAll();
        if (selectedItems.isEmpty()) {
            realm.commitTransaction();
            return;
        }
        realm.commitTransaction();

        new CustomAlertDialog()
                .setTitle(getString(R.string.list_deletion))
                .setMessage(getString(R.string.list_deletion_confirmation_message))
                .setPositiveListener(getString(R.string.yes), new CustomAlertDialog.PositiveListener() {
                    @Override
                    public void onNegativeClicked(CustomAlertDialog dialog) {
                        realm.beginTransaction();
                        RealmResults<Item> selectedItems = realm.where(Item.class).equalTo("checked", true).findAll();
                        selectedItems.deleteAllFromRealm();
                        realm.commitTransaction();

                        loadData();
                    }
                })
                .setNegativeListener(getString(R.string.cancel), new CustomAlertDialog.NegativeListener() {
                    @Override
                    public void onNegativeClicked(CustomAlertDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(getFragmentManager(), "deletion_dialog");
    }

    private int getNextKey() {
        if (realm.where(Item.class).max("id") == null) {
            return 1;
        }
        return realm.where(Item.class).max("id").intValue() + 1;
    }
}
