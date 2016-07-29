package com.ostro.myshoppinglist.base.clickable_recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Nicolas Dumont
 * nicolas@tymate.com
 * on 24/07/15.
 */
public abstract class ClickableHolder extends RecyclerView.ViewHolder {

    public ClickableHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        });
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener onItemLongClickListener) {
        this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener == null) {
                    return false;
                }
                onItemLongClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });
    }
}