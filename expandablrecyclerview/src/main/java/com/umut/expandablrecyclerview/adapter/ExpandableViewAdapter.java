package com.umut.expandablrecyclerview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.umut.expandablrecyclerview.adapter.data.ExpandableDataProvider;
import com.umut.expandablrecyclerview.adapter.index.ComputingExpandableIndexProvider;
import com.umut.expandablrecyclerview.adapter.index.AdapterIndexConverter;

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
    public int getItemCount() {
        return adapterIndexConverter.calculateTotalItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return adapterIndexConverter.findViewType(position);
    }

    protected abstract ExpandableViewHolder<P> createParentViewHolder(@NonNull ViewGroup container);

    protected abstract ExpandableViewHolder<C> createChildViewHolder(@NonNull ViewGroup container);

    public void toggle(int index) {
        if (index < 0 || index > dataProvider.getParentSize()) {
            throw new IllegalArgumentException("Cant expand the given index. Parent Size = " +
                    dataProvider.getParentSize() + ", index = " + index);
        }
        if (!adapterIndexConverter.isParentExpanded(index)) {
            expand(index);
        } else {
            collapse(index);
        }
    }

    public void notifyParent(int parentDataIndex) {
        int countTillParentIndex = adapterIndexConverter.getCountTillParentIndex(parentDataIndex);
        notifyItemChanged(countTillParentIndex);
    }

    private void expand(int index) {
        P p = dataProvider.getParent(index);
        int itemCountUpToParent = adapterIndexConverter.getCountTillParentIndex(index);
        notifyItemRangeInserted(itemCountUpToParent + 1, dataProvider.getChildrenSize(p));
        adapterIndexConverter.expandParent(index);
    }

    private void collapse(int index) {
        P p = dataProvider.getParent(index);
        int itemCountUpToParent = adapterIndexConverter.getCountTillParentIndex(index);
        notifyItemRangeRemoved(itemCountUpToParent + 1, dataProvider.getChildrenSize(p));
        adapterIndexConverter.collapseParent(index);
    }
}
