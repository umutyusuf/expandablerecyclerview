package com.umut.expandablrecyclerview.adapter.index;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType.CHILD;
import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType.PARENT;

public interface ExpandableIndexProvider {

    int getTotalItemCount();

    int getAdapterIndexForParentAt(int adapterParentIndex);

    @ViewType
    int getViewType(int position);

    int parentIndexFromAdapterPosition(int adapterPosition);

    boolean isExpanded(int dataPosition);

    void onExpand(int dataPosition);

    void onCollapse(int dataPosition);

    @Nullable
    ChildCoordinate getChildCoordinateFromAdapterIndex(int adapterPosition);

    @IntDef({PARENT, CHILD})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
        int PARENT = 0;
        int CHILD = 1;
    }
}
