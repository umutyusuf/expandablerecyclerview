package com.umut.expandablrecyclerview.adapter;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider;
import com.umut.expandablrecyclerview.adapter.holder.ChildViewHolder;
import com.umut.expandablrecyclerview.adapter.holder.ExpandableViewHolder;
import com.umut.expandablrecyclerview.adapter.holder.ParentViewHolder;
import com.umut.expandablrecyclerview.adapter.index.AdapterIndexConverter;
import com.umut.expandablrecyclerview.adapter.index.ComputingExpandableIndexProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.COLLAPSED;
import static com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.EXPANDED;
import static com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType.PARENT;

public abstract class ExpandableViewAdapter extends RecyclerView.Adapter<ExpandableViewHolder> {

    @NonNull
    private final AdapterIndexConverter adapterIndexConverter;

    @NonNull
    private final ExpandableDataIndexProvider dataProvider;


    public ExpandableViewAdapter(@NonNull ExpandableDataIndexProvider dataProvider) {
        this.dataProvider = dataProvider;
        this.adapterIndexConverter =
                new AdapterIndexConverter(new ComputingExpandableIndexProvider(dataProvider));
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return viewType == PARENT ? createParentViewHolder(parent) :
                createChildViewHolder(parent);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ExpandableViewHolder holder, int position) {
        if (holder instanceof ParentViewHolder) {
            int parentPosition = adapterIndexConverter.getParentPosition(position);
            ((ParentViewHolder) holder).bind(parentPosition,
                    adapterIndexConverter.isParentExpanded(parentPosition) ? EXPANDED : COLLAPSED);
        } else if (holder instanceof ChildViewHolder) {
            ((ChildViewHolder) holder).bind(adapterIndexConverter.getChildCoordinate(position));
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

    protected abstract ParentViewHolder createParentViewHolder(@NonNull ViewGroup container);

    protected abstract ChildViewHolder createChildViewHolder(@NonNull ViewGroup container);

    public void toggle(int index) {
        if (index < 0 || index > dataProvider.getParentSize()) {
            throw new IllegalArgumentException("Cant expand the given index. Parent Size = " +
                    dataProvider.getParentSize() + ", index = " + index);
        }
        int parentAdapterIndex;
        if (!adapterIndexConverter.isParentExpanded(index)) {
            parentAdapterIndex = expand(index);
        } else {
            parentAdapterIndex = collapse(index);
        }
        notifyItemChanged(parentAdapterIndex, adapterIndexConverter.isParentExpanded(index) ?
                EXPANDED : COLLAPSED);
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

    private int expand(int index) {
        int parentAdapterIndex = adapterIndexConverter.getParentAdapterIndex(index);
        notifyItemRangeInserted(parentAdapterIndex + 1, dataProvider.getChildrenSize(index));
        adapterIndexConverter.expandParent(index);
        return parentAdapterIndex;
    }

    private int collapse(int index) {
        int parentAdapterIndex = adapterIndexConverter.getParentAdapterIndex(index);
        notifyItemRangeRemoved(parentAdapterIndex + 1, dataProvider.getChildrenSize(index));
        adapterIndexConverter.collapseParent(index);
        return parentAdapterIndex;
    }

    @IntDef({EXPANDED, COLLAPSED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int EXPANDED = 0x0001;
        int COLLAPSED = 0x0010;
    }
}
