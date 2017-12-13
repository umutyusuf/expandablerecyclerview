package com.umut.expandablrecyclerview.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.umut.expandablrecyclerview.adapter.data.ExpandableDataProvider;
import com.umut.expandablrecyclerview.adapter.index.AdapterIndexConverter;
import com.umut.expandablrecyclerview.adapter.index.ComputingExpandableIndexProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import static com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.COLLAPSED;
import static com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.EXPANDED;
import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType.CHILD;
import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType.PARENT;

public abstract class ExpandableViewAdapter<P, C> extends RecyclerView.Adapter<ExpandableViewHolder> {

    @NonNull
    private final AdapterIndexConverter adapterIndexConverter;

    @NonNull
    private final ExpandableDataProvider<P, C> dataProvider;

    public ExpandableViewAdapter(@NonNull ExpandableDataProvider<P, C> dataProvider) {
        this.dataProvider = dataProvider;
        this.adapterIndexConverter =
                new AdapterIndexConverter(new ComputingExpandableIndexProvider<>(dataProvider));
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == PARENT ? createParentViewHolder(parent) :
                createChildViewHolder(parent);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(ExpandableViewHolder holder, int position) {
        int viewType = adapterIndexConverter.findViewType(position);
        switch (viewType) {
            case CHILD:
                ChildCoordinate childCoordinate = adapterIndexConverter.getChildCoordinate(position);
                holder.bind(dataProvider.getChild(childCoordinate), childCoordinate.parentIndex);
                break;
            case PARENT:
                int parentPosition = adapterIndexConverter.getParentPosition(position);
                holder.bind(dataProvider.getParent(parentPosition), parentPosition);
        }
    }

    @Override
    public void onBindViewHolder(ExpandableViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        }
        int viewType = adapterIndexConverter.findViewType(position);
        switch (viewType) {
            case CHILD:
                ChildCoordinate childCoordinate = adapterIndexConverter.getChildCoordinate(position);
                holder.bindPartial(payloads, childCoordinate.parentIndex);
                break;
            case PARENT:
                int parentPosition = adapterIndexConverter.getParentPosition(position);
                holder.bindPartial(payloads, parentPosition);
        }
    }

    @Override
    public int getItemCount() {
        return adapterIndexConverter.calculateTotalItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return adapterIndexConverter.findViewType(position);
    }

    protected abstract ExpandableViewHolder<P> createParentViewHolder(@NonNull ViewGroup container);

    protected abstract ExpandableViewHolder<C> createChildViewHolder(@NonNull ViewGroup container);

    @State
    public int toggle(int index) {
        if (index < 0 || index > dataProvider.getParentSize()) {
            throw new IllegalArgumentException("Cant expand the given index. Parent Size = " +
                    dataProvider.getParentSize() + ", index = " + index);
        }
        if (!adapterIndexConverter.isParentExpanded(index)) {
            expand(index);
            return EXPANDED;
        } else {
            collapse(index);
            return COLLAPSED;
        }
    }

    public void notifyParent(int parentDataIndex) {
        int adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex);
        notifyItemChanged(adapterParentIndex);
    }


    public void notifyParent(int parentDataIndex, @Nullable Object payload) {
        int adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex);
        notifyItemChanged(adapterParentIndex, payload);
    }

    public void notifyChild(@NonNull ChildCoordinate childCoordinate) {
        int adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex);
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1);
    }


    public void notifyChild(@NonNull ChildCoordinate childCoordinate, @Nullable Object payload) {
        int adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex);
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1, payload);
    }

    private void expand(int index) {
        P p = dataProvider.getParent(index);
        int itemCountUpToParent = adapterIndexConverter.getParentAdapterIndex(index);
        notifyItemRangeInserted(itemCountUpToParent + 1, dataProvider.getChildrenSize(p));
        adapterIndexConverter.expandParent(index);
    }

    private void collapse(int index) {
        P p = dataProvider.getParent(index);
        int itemCountUpToParent = adapterIndexConverter.getParentAdapterIndex(index);
        notifyItemRangeRemoved(itemCountUpToParent + 1, dataProvider.getChildrenSize(p));
        adapterIndexConverter.collapseParent(index);
    }

    @IntDef({EXPANDED, COLLAPSED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int EXPANDED = -1;
        int COLLAPSED = 1;
    }
}
