package com.umut.expandablrecyclerview.adapter;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

public abstract class ExpandableViewHolder<T> extends RecyclerView.ViewHolder {

    public ExpandableViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * binds the value to view holder
     *
     * @param value              value for the item for calculated position
     * @param parentDataPosition position of the parent in data
     */
    public abstract void bind(T value, int parentDataPosition);

    public void bindPartial(@NonNull List<Object> value, int parentDataPosition) {

    }
}
