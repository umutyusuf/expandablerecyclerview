package com.umut.expandablrecyclerview.adapter.index;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataProvider;
import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter;

import java.util.HashSet;
import java.util.Set;

public class ComputingExpandableIndexProvider<T> implements ExpandableIndexProvider {

    @NonNull
    private final ExpandableDataProvider<T, ?> dataProvider;

    @NonNull
    private final Set<T> expandedParents;

    public ComputingExpandableIndexProvider(@NonNull ExpandableDataProvider<T, ?> dataProvider) {
        this.dataProvider = dataProvider;
        expandedParents = new HashSet<>();
    }

    @Override
    public int getTotalItemCount() {
        int totalCount = dataProvider.getParentSize();
        for (T parent : expandedParents) {
            totalCount += dataProvider.getChildren(parent).size();
        }
        return totalCount;
    }

    @Override
    public int getAdapterIndexForParentAt(int adapterParentIndex) {
        int currentCount = adapterParentIndex;
        T parent;
        for (int i = 0; i < adapterParentIndex; i++) {
            parent = dataProvider.getParent(i);
            if (expandedParents.contains(parent)) {
                currentCount += dataProvider.getChildren(parent).size();
            }
        }
        return currentCount;
    }

    @Override
    public int getViewType(int position) {
        int nextParentIndex = 0;
        T parent;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            if (position == nextParentIndex) {
                return ViewType.PARENT;
            }
            parent = dataProvider.getParent(i);
            nextParentIndex++;
            if (expandedParents.contains(parent)) {
                nextParentIndex += dataProvider.getChildren(parent).size();
            }
        }
        return ViewType.CHILD;
    }

    @Override
    public int parentIndexFromAdapterPosition(int adapterPosition) {
        int nextParentIndex = 0;
        T parent;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            if (adapterPosition == nextParentIndex) {
                return i;
            }
            parent = dataProvider.getParent(i);
            nextParentIndex++;
            if (expandedParents.contains(parent)) {
                nextParentIndex += dataProvider.getChildren(parent).size();
            }
        }
        Log.e(ExpandableViewAdapter.class.getName(), "Parent Value mismatch");
        return 0;
    }

    @Override
    public boolean isExpanded(int dataPosition) {
        return expandedParents.contains(dataProvider.getParent(dataPosition));
    }

    @Override
    public void onExpand(int dataPosition) {
        this.expandedParents.add(dataProvider.getParent(dataPosition));
    }

    @Override
    public void onCollapse(int dataPosition) {
        this.expandedParents.remove(dataProvider.getParent(dataPosition));
    }

    @Override
    @Nullable
    public ChildCoordinate getChildCoordinateFromAdapterIndex(int adapterPosition) {
        int totalItemCount = 0;
        T p;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            p = dataProvider.getParent(i);
            if (adapterPosition < totalItemCount) {
                return getChildCoordinate(i - 1, adapterPosition, totalItemCount);
            }
            totalItemCount++;
            totalItemCount += expandedParents.contains(p) ? dataProvider.getChildren(p).size() : 0;
        }
        if (adapterPosition < totalItemCount) {
            try {
                return getChildCoordinate((dataProvider.getParentSize() - 1), adapterPosition,
                        totalItemCount);
            } catch (Exception e) {
                Log.e(ExpandableViewAdapter.class.getName(), "Failed to get child");
            }
        }
        return null;
    }

    private ChildCoordinate getChildCoordinate(int parentIndex, int adapterPosition,
                                               int markedItemCount) {
        T previousParent = dataProvider.getParent(parentIndex);
        int childPositionInParent = adapterPosition -
                (markedItemCount - dataProvider.getChildren(previousParent).size());
        return new ChildCoordinate(parentIndex, childPositionInParent);
    }
}
