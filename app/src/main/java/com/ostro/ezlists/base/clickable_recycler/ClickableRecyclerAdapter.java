package com.ostro.ezlists.base.clickable_recycler;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas Dumont
 * nicolas@tymate.com
 * on 24/07/15.
 */
public abstract class ClickableRecyclerAdapter<O, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    @NonNull
    protected List<O> mItems = new ArrayList<>();

    public ClickableRecyclerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener) {
        mOnItemClickListener = onClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onLongClickListener) {
        mOnItemLongClickListener = onLongClickListener;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public boolean isEmpty() {
        return mItems.isEmpty();
    }

    public void clearAll() {
        for (int i=0; i<mItems.size(); i++) {
            removeItem(i);
        }
    }

    @Nullable
    public O getItem(int position) {
        try {
            return mItems.get(position);
        } catch (Exception e) {
            return null;
        }
    }

    public void removeItem(int position) {
        try {
            mItems.remove(position);
        } catch (Exception e) {
        }
    }

    public void setItems(@NonNull List<O> list) {
        mItems = list;
    }

    public List<O> getItems() {
        return mItems;
    }

    public void addItems(@NonNull List<O> list) {
        mItems.addAll(list);
    }

    public void addItem(@NonNull O o) {
        mItems.add(o);
    }

    protected abstract int getHolderResources(int viewType);
    protected abstract VH getHolderView(View view, int viewType);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        VH vh = getHolderView(mLayoutInflater.inflate(getHolderResources(viewType), parent, false), viewType);
        if(vh instanceof ClickableHolder) {
            ((ClickableHolder) vh).setOnItemClickListener(mOnItemClickListener);
            ((ClickableHolder) vh).setOnItemLongClickListener(mOnItemLongClickListener);
        }
        return vh;
    }
}
