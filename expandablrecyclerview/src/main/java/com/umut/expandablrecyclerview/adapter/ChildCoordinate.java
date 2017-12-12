package com.umut.expandablrecyclerview.adapter;

public class ChildCoordinate {

    public final int parentIndex;
    public final int childRelativeIndex;

    public ChildCoordinate(int parentIndex, int childRelativeIndex) {
        this.parentIndex = parentIndex;
        this.childRelativeIndex = childRelativeIndex;
    }
}
