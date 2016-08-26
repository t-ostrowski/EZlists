package com.ostro.ezlists.ui.list;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ostro.ezlists.R;
import com.ostro.ezlists.base.ToolbarActivity;
import com.ostro.ezlists.model.List;
import com.ostro.ezlists.ui.list.dialog.DialogCloseListener;
import com.ostro.ezlists.ui.list.dialog.EditListDialog;
import com.ostro.ezlists.ui.list.dialog.EditListDialogOpenListener;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class ListActivity extends ToolbarActivity implements EditListDialogOpenListener,
        DialogCloseListener {

    @BindView(R.id.rv_list)
    RecyclerView rvLists;
    @BindView(R.id.tv_empty_state)
    TextView tvEmptyState;
    @BindView(R.id.et_new_list)
    EditText etNewList;
    @BindView(R.id.btn_add_list)
    ImageView btnAddList;

    private ListAdapter adapter;
    private Realm realm;

    @Override
    public int getLayoutResources() {
        return R.layout.activity_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getString(R.string.list_your_lists));
        realm = Realm.getDefaultInstance();
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initRecycler(this);

        etNewList.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (!TextUtils.isEmpty(etNewList.getText().toString())) {
                        List listToAdd = new List(etNewList.getText().toString().trim());
                        addList(listToAdd);
                    }
                    handled = true;
                }
                return handled;
            }
        });
        displayEmptyStateIfEmpty(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Toolbar toolbar = getToolbar();
        if (toolbar != null) {
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public void handleOpenEditDialog(long listId) {
        showEditListDialog(listId);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        dismissDialog("edit_list_dialog");
        refreshList();
    }

    public void dismissDialog(String tag) {
        Fragment prev = getFragmentManager().findFragmentByTag(tag);
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
    }

    private void refreshList() {
        for (int i=0; i<adapter.getItems().size(); i++) {
            adapter.removeItem(i);
        }
        loadData();
    }

    private void showEditListDialog(long listId) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        DialogFragment assignmentDialogFragment = EditListDialog.newInstance(listId);
        assignmentDialogFragment.show(ft, "edit_list_dialog");
    }

    @OnClick(R.id.btn_add_list)
    public void onAddItemClick() {
        if (!TextUtils.isEmpty(etNewList.getText().toString())) {
            List listToAdd = new List(etNewList.getText().toString().trim());
            addList(listToAdd);
        }
    }

    private void initRecycler(Context context) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new ListAdapter(context, this);

        rvLists.setLayoutManager(layoutManager);
        rvLists.setAdapter(adapter);
    }

    private void loadData() {
        RealmResults<List> results = realm.where(List.class).findAll();
        adapter.setItems(results);
        adapter.notifyDataSetChanged();
        displayEmptyStateIfEmpty(adapter);
    }

    private void addList(List list) {
        realm.beginTransaction();
        list.setId(List.getNextKey());
        realm.copyToRealm(list);
        realm.commitTransaction();

        loadData();
    }

    private void displayEmptyStateIfEmpty(ListAdapter adapter) {
        if (adapter.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            etNewList.setText("");
        }
    }
}
