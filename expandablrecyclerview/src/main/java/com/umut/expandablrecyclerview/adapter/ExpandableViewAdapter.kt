package com.umut.expandablrecyclerview.adapter

import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.recyclerview.widget.RecyclerView
import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.Companion.COLLAPSED
import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State.Companion.EXPANDED
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider
import com.umut.expandablrecyclerview.adapter.holder.ChildViewHolder
import com.umut.expandablrecyclerview.adapter.holder.ExpandableViewHolder
import com.umut.expandablrecyclerview.adapter.holder.ParentViewHolder
import com.umut.expandablrecyclerview.adapter.index.AdapterIndexConverter
import com.umut.expandablrecyclerview.adapter.index.ComputingExpandableIndexProvider
import com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider

abstract class ExpandableViewAdapter(private val dataProvider: ExpandableDataIndexProvider) : RecyclerView.Adapter<ExpandableViewHolder<*>>() {

    private val adapterIndexConverter: AdapterIndexConverter =
            AdapterIndexConverter(ComputingExpandableIndexProvider(dataProvider))


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder<*> {
        return if (viewType == ExpandableIndexProvider.PARENT)
            createParentViewHolder(parent)
        else
            createChildViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder<*>, position: Int) {
        if (holder is ParentViewHolder) {
            val parentPosition = adapterIndexConverter.getParentPosition(position)
            holder.bind(parentPosition,
                    if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED)
        } else if (holder is ChildViewHolder) {
            holder.bind(adapterIndexConverter.getChildCoordinate(position)!!)
        }
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder<*>, position: Int, payloads: List<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        if (payloads[0] is Int && isStateChange(payloads[0] as Int)) {
            var binded = false
            if (holder is ParentViewHolder) {
                val parentPosition = adapterIndexConverter.getParentPosition(position)
                binded = holder.update(payloads, parentPosition,
                        if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED)
            } else if (holder is ChildViewHolder) {
                binded = holder
                        .update(payloads, adapterIndexConverter.getChildCoordinate(position)!!)
            }
            if (!binded) {
                super.onBindViewHolder(holder, position, payloads)
            }
            return
        }
        if (holder !is ParentViewHolder) {
            return
        }
        val stateChanged: Boolean
        val parentPosition = adapterIndexConverter.getParentPosition(position)
        stateChanged = holder.onStateChanged(parentPosition,
                if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED)

        if (!stateChanged) {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun getItemCount(): Int {
        return adapterIndexConverter.calculateTotalItemCount()
    }

    override fun getItemViewType(position: Int): Int {
        return adapterIndexConverter.findViewType(position)
    }

    protected abstract fun createParentViewHolder(container: ViewGroup): ParentViewHolder

    protected abstract fun createChildViewHolder(container: ViewGroup): ChildViewHolder

    fun toggle(index: Int) {
        require(!(index < 0 || index > dataProvider.parentSize)) {
            "Cant expand the given index. Parent Size = " +
                    dataProvider.parentSize + ", index = " + index
        }
        val parentAdapterIndex = if (!adapterIndexConverter.isParentExpanded(index)) {
            expand(index)
        } else {
            collapse(index)
        }
        notifyItemChanged(parentAdapterIndex, if (adapterIndexConverter.isParentExpanded(index))
            EXPANDED
        else
            COLLAPSED)
    }

    fun notifyParent(parentDataIndex: Int) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex)
        notifyItemChanged(adapterParentIndex)
    }

    fun notifyParent(parentDataIndex: Int, payload: Any?) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex)
        notifyItemChanged(adapterParentIndex, payload)
    }


    fun notifyChild(childCoordinate: ChildCoordinate) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex)
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1)
    }

    fun notifyChild(childCoordinate: ChildCoordinate, payload: Any?) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex)
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1, payload)
    }

    fun flushCahce() {
        adapterIndexConverter.flushCache()
    }


    private fun isStateChange(value: Int?): Boolean {
        return value?.xor(EXPANDED) == 0 || value?.xor(COLLAPSED) == 0
    }

    private fun expand(index: Int): Int {
        val parentAdapterIndex = adapterIndexConverter.getParentAdapterIndex(index)
        notifyItemRangeInserted(parentAdapterIndex + 1, dataProvider.getChildrenSize(index))
        adapterIndexConverter.expandParent(index)
        return parentAdapterIndex
    }

    private fun collapse(index: Int): Int {
        val parentAdapterIndex = adapterIndexConverter.getParentAdapterIndex(index)
        notifyItemRangeRemoved(parentAdapterIndex + 1, dataProvider.getChildrenSize(index))
        adapterIndexConverter.collapseParent(index)
        return parentAdapterIndex
    }

    @IntDef(EXPANDED, COLLAPSED)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class State {
        companion object {
            const val EXPANDED = 0x0001
            const val COLLAPSED = 0x0010
        }
    }
}
