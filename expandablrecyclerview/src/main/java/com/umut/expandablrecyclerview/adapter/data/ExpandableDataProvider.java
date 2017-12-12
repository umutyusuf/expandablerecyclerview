package com.umut.expandablrecyclerview.adapter.data;

import android.support.annotation.NonNull;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;

import java.util.List;

public interface ExpandableDataProvider<P, C> {

    @NonNull
    List<C> getChildren(@NonNull P parent);

    @NonNull
    C getChild(@NonNull ChildCoordinate coordinate);

    int getChildrenSize(P parent);

    int getParentSize();

    P getParent(int index);

}
