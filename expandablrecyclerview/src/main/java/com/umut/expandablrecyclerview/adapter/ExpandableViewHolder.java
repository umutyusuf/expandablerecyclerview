package com.umut.expandablrecyclerview.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class ExpandableViewHolder<T> extends RecyclerView.ViewHolder {

    public ExpandableViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * binds the value to view holder
     * @param value value for the item for calculated position
     * @param position position of the parent in data
     */
    public abstract void bind(T value, int position);
}
