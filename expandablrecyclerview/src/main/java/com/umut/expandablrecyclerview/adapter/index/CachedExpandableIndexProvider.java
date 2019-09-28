package com.umut.expandablrecyclerview.adapter.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;

public class CachedExpandableIndexProvider implements ExpandableIndexProvider {

    protected static final int UNSPECIFIED = -1;

    private int totalItemCount;
    @NonNull
    private final SparseIntArray parentPositionCache;
    @NonNull
    private final SparseArray<ChildCoordinate> childCoordinateCache;
    @NonNull
    private final SparseIntArray viewTypeCache;

    public CachedExpandableIndexProvider() {
        this.parentPositionCache = new SparseIntArray();
        this.childCoordinateCache = new SparseArray<>();
        this.viewTypeCache = new SparseIntArray();
        totalItemCount = UNSPECIFIED;
    }

    @Override
    public int getTotalItemCount() {
        return totalItemCount;
    }

    @Override
    public int getAdapterIndexForParentAt(int adapterParentIndex) {
        throw new UnsupportedOperationException("This operation is not supported.");
    }

    @Override
    public int getViewType(int position) {
        return viewTypeCache.get(position, UNSPECIFIED);
    }

    @Override
    public int parentIndexFromAdapterPosition(int adapterPosition) {
        return parentPositionCache.get(adapterPosition, UNSPECIFIED);
    }

    @Override
    public boolean isExpanded(int dataPosition) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void onExpand(int dataPosition) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public void onCollapse(int dataPosition) {
        throw new UnsupportedOperationException("This operation is not supported");
    }

    @Override
    public ChildCoordinate getChildCoordinateFromAdapterIndex(int adapterPosition) {
        return childCoordinateCache.get(adapterPosition);
    }

    void flushCache() {
        totalItemCount = UNSPECIFIED;
        parentPositionCache.clear();
        childCoordinateCache.clear();
        viewTypeCache.clear();
    }

    void cacheParentPosition(int position, int parentIndex) {
        parentPositionCache.append(position, parentIndex);
    }

    void cacheChildCoordinate(int position, @Nullable ChildCoordinate coordinate) {
        childCoordinateCache.append(position, coordinate);
    }

    void cacheTotalItemCount(int totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    void cacheViewType(int position, @ViewType int viewType) {
        this.viewTypeCache.append(position, viewType);
    }
}
