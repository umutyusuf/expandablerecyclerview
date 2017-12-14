package com.umut.expandablrecyclerview.adapter.data;

public interface ExpandableDataIndexProvider {

    int getChildrenSize(int parentIndex);

    int getParentSize();

}
