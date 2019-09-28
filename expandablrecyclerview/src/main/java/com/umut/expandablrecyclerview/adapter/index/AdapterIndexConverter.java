package com.umut.expandablrecyclerview.adapter.index;


import androidx.annotation.NonNull;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;

import static com.umut.expandablrecyclerview.adapter.index.CachedExpandableIndexProvider.UNSPECIFIED;
import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType;

public class AdapterIndexConverter {

    @NonNull
    private final CachedExpandableIndexProvider cachedIndexProvider;

    @NonNull
    private final ExpandableIndexProvider defaultIndexProvider;

    public AdapterIndexConverter(@NonNull ExpandableIndexProvider computingIndexProvider) {
        this.defaultIndexProvider = computingIndexProvider;
        cachedIndexProvider = new CachedExpandableIndexProvider();
    }

    public boolean isParentExpanded(int dataPosition) {
        return defaultIndexProvider.isExpanded(dataPosition);
    }

    public void expandParent(int dataPosition) {
        defaultIndexProvider.onExpand(dataPosition);
        flushCache();
    }

    public void collapseParent(int dataPosition) {
        defaultIndexProvider.onCollapse(dataPosition);
        flushCache();
    }

    public int calculateTotalItemCount() {
        if (cachedIndexProvider.getTotalItemCount() != UNSPECIFIED) {
            return cachedIndexProvider.getTotalItemCount();
        }
        int totalItemCount = defaultIndexProvider.getTotalItemCount();
        cachedIndexProvider.cacheTotalItemCount(totalItemCount);
        return totalItemCount;
    }

    public int getParentAdapterIndex(int index) {
        return defaultIndexProvider.getAdapterIndexForParentAt(index);
    }

    @ViewType
    public int findViewType(int index) {
        if (cachedIndexProvider.getViewType(index) != UNSPECIFIED) {
            return cachedIndexProvider.getViewType(index);
        }
        int viewType = defaultIndexProvider.getViewType(index);
        cachedIndexProvider.cacheViewType(index, viewType);
        return viewType;
    }

    public int getParentPosition(int index) {
        if (cachedIndexProvider.parentIndexFromAdapterPosition(index) != UNSPECIFIED) {
            return cachedIndexProvider.parentIndexFromAdapterPosition(index);
        }
        int parentPosition = defaultIndexProvider.parentIndexFromAdapterPosition(index);
        cachedIndexProvider.cacheParentPosition(index, parentPosition);
        return parentPosition;
    }

    public ChildCoordinate getChildCoordinate(int index) {
        if (cachedIndexProvider.getChildCoordinateFromAdapterIndex(index) != null) {
            return cachedIndexProvider.getChildCoordinateFromAdapterIndex(index);
        }
        ChildCoordinate childCoordinate = defaultIndexProvider
                .getChildCoordinateFromAdapterIndex(index);
        cachedIndexProvider.cacheChildCoordinate(index, childCoordinate);
        return childCoordinate;
    }

    public void flushCache() {
        cachedIndexProvider.flushCache();
    }
}
