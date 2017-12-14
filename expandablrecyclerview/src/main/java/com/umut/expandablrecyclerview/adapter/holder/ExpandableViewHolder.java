package com.umut.expandablrecyclerview.adapter.holder;


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
     * @param coordinate Coordinate of item
     */
    public abstract void bind(@NonNull T coordinate);

    public abstract boolean update(@NonNull List<Object> payload, @NonNull T coordinate);
}
