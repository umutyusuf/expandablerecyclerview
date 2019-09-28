package com.umut.expandablrecyclerview.adapter.holder;

import androidx.annotation.NonNull;
import android.view.View;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;

import java.util.List;

public abstract class ChildViewHolder extends ExpandableViewHolder<ChildCoordinate> {

    public ChildViewHolder(View itemView) {
        super(itemView);
    }

    /**
     * binds the value to view holder
     *
     * @param coordinate Coordinate of item
     */
    public abstract void bind(@NonNull ChildCoordinate coordinate);

    public boolean update(@NonNull List<Object> payload, ChildCoordinate coordinate) {
        return false;
    }
}
