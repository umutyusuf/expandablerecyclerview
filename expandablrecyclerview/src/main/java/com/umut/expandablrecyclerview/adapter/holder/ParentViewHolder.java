package com.umut.expandablrecyclerview.adapter.holder;


import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

public abstract class ParentViewHolder extends ExpandableViewHolder<Integer> {

    public ParentViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public boolean update(@NonNull List<Object> payload, @NonNull Integer coordinate) {
        return false;
    }
}
