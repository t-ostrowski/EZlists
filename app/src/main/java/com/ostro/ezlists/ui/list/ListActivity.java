package com.ostro.ezlists.ui.list;

import android.content.Context;
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

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 * on 25/08/2016.
 */

public class ListActivity extends ToolbarActivity {

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
    public void onStop() {
        realm.close();
        super.onStop();
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

    @OnClick(R.id.btn_add_list)
    public void onAddItemClick() {
        if (!TextUtils.isEmpty(etNewList.getText().toString())) {
            List listToAdd = new List(etNewList.getText().toString().trim());
            addList(listToAdd);
        }
    }

    private void initRecycler(Context context) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        adapter = new ListAdapter(context);

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