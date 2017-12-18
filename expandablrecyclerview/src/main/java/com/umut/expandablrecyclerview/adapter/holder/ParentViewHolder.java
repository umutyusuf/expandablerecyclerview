package com.umut.expandablrecyclerview.adapter.holder;


import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import static com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State;

public abstract class ParentViewHolder extends ExpandableViewHolder<Integer> {

    public ParentViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * binds the value to view holder
     *
     * @param coordinate Coordinate of item
     * @param state      state of parent (expanded or collapsed)
     */
    public abstract void bind(int coordinate, @State int state);

    public boolean update(@NonNull List<Object> payload, int coordinate, @State int state) {
        return false;
    }
}
