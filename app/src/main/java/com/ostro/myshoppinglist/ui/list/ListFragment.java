package com.ostro.myshoppinglist.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.ostro.myshoppinglist.R;
import com.ostro.myshoppinglist.base.BaseFragment;
import com.ostro.myshoppinglist.model.Item;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private ItemAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.setDebug(true);
        setTitle("Liste");
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
    public void onStart() {
        super.onStart();
        displayEmptyStateIfEmpty(adapter);
    }

    @Override
    public int getLayoutResources() {
        return R.layout.fragment_list;
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
        }
    }

    private void addItemToList(Item item) {
        adapter.addItem(item);
        adapter.notifyDataSetChanged();
        displayEmptyStateIfEmpty(adapter);
        etNewItem.setText("");
    }
}
