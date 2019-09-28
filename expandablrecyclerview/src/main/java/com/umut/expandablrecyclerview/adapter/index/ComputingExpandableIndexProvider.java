package com.umut.expandablrecyclerview.adapter.index;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider;

import java.util.HashSet;
import java.util.Set;

public class ComputingExpandableIndexProvider implements ExpandableIndexProvider {

    @NonNull
    private final ExpandableDataIndexProvider dataProvider;

    @NonNull
    private final Set<Integer> expandedParents;

    public ComputingExpandableIndexProvider(@NonNull ExpandableDataIndexProvider dataProvider) {
        this.dataProvider = dataProvider;
        expandedParents = new HashSet<>();
    }

    @Override
    public int getTotalItemCount() {
        int totalCount = dataProvider.getParentSize();
        for (Integer parent : expandedParents) {
            totalCount += dataProvider.getChildrenSize(parent);
        }
        return totalCount;
    }

    @Override
    public int getAdapterIndexForParentAt(int adapterParentIndex) {
        int currentCount = adapterParentIndex;
        for (int i = 0; i < adapterParentIndex; i++) {
            if (expandedParents.contains(i)) {
                currentCount += dataProvider.getChildrenSize(i);
            }
        }
        return currentCount;
    }

    @Override
    public int getViewType(int position) {
        int nextParentIndex = 0;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            if (position == nextParentIndex) {
                return ViewType.PARENT;
            }
            nextParentIndex++;
            if (expandedParents.contains(i)) {
                nextParentIndex += dataProvider.getChildrenSize(i);
            }
        }
        return ViewType.CHILD;
    }

    @Override
    public int parentIndexFromAdapterPosition(int adapterPosition) {
        int nextParentIndex = 0;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            if (adapterPosition == nextParentIndex) {
                return i;
            }
            nextParentIndex++;
            if (expandedParents.contains(i)) {
                nextParentIndex += dataProvider.getChildrenSize(i);
            }
        }
        throw new IllegalStateException("Parent index can not be computed");
    }

    @Override
    public boolean isExpanded(int dataPosition) {
        return expandedParents.contains(dataPosition);
    }

    @Override
    public void onExpand(int dataPosition) {
        this.expandedParents.add(dataPosition);
    }

    @Override
    public void onCollapse(int dataPosition) {
        this.expandedParents.remove(dataPosition);
    }

    @Override
    @Nullable
    public ChildCoordinate getChildCoordinateFromAdapterIndex(int adapterPosition) {
        int totalItemCount = 0;
        for (int i = 0; i < dataProvider.getParentSize(); i++) {
            if (adapterPosition < totalItemCount) {
                return getChildCoordinate(i - 1, adapterPosition, totalItemCount);
            }
            totalItemCount++;
            totalItemCount += expandedParents.contains(i) ? dataProvider.getChildrenSize(i) : 0;
        }
        if (adapterPosition < totalItemCount) {
            try {
                return getChildCoordinate((dataProvider.getParentSize() - 1), adapterPosition,
                        totalItemCount);
            } catch (Exception e) {
                throw new IllegalStateException("Child Coordinate can not be computed", e);
            }
        }
        throw new IllegalStateException("Child Coordinate can not be computed");
    }

    private ChildCoordinate getChildCoordinate(int parentIndex, int adapterPosition,
                                               int markedItemCount) {
        int childPositionInParent = adapterPosition -
                (markedItemCount - dataProvider.getChildrenSize(parentIndex));
        return new ChildCoordinate(parentIndex, childPositionInParent);
    }
}
