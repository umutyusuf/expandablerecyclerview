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

/**
 * Custom adapter handles item expand/collapse.
 * Expects [ExpandableIndexProvider] to decide which view to show.
 *
 * Only requires the implementation for
 *  -> [createParentViewHolder]
 *  -> [createChildViewHolder]
 *  implementations to facilitate properly.
 *
 *  @note [#onBindViewHolder(ExpandableViewHolder<*>, Int, List<Any>)]
 *  method with payload [List<Integer>] is reserved to handle expand/collapse state changes.
 *  If you need to perform a re-render an item with a payload,
 *  consider a payload type other than [Integer]
 */
abstract class ExpandableViewAdapter(private val dataProvider: ExpandableDataIndexProvider) :
    RecyclerView.Adapter<ExpandableViewHolder<*>>() {

    private val adapterIndexConverter: AdapterIndexConverter =
        AdapterIndexConverter(ComputingExpandableIndexProvider(dataProvider))


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpandableViewHolder<*> {
        val viewTypeDetail = adapterIndexConverter.extractViewTypeDetail(viewType)
        return if (viewTypeDetail.isParent)
            createParentViewHolder(parent, viewTypeDetail.externalViewType)
        else
            createChildViewHolder(parent, viewTypeDetail.externalViewType)
    }

    override fun onBindViewHolder(holder: ExpandableViewHolder<*>, position: Int) {
        if (holder is ParentViewHolder) {
            val parentPosition = adapterIndexConverter.getParentPosition(position)
            holder.bind(
                parentPosition,
                if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED
            )
        } else if (holder is ChildViewHolder) {
            holder.bind(adapterIndexConverter.getChildCoordinate(position)!!)
        }
    }

    override fun onBindViewHolder(
        holder: ExpandableViewHolder<*>,
        position: Int,
        payloads: List<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        if (payloads[0] is Int && isStateChange(payloads[0] as Int)) {
            var binded = false
            if (holder is ParentViewHolder) {
                val parentPosition = adapterIndexConverter.getParentPosition(position)
                binded = holder.update(
                    payloads, parentPosition,
                    if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED
                )
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
        stateChanged = holder.onStateChanged(
            parentPosition,
            if (adapterIndexConverter.isParentExpanded(parentPosition)) EXPANDED else COLLAPSED
        )

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

    protected abstract fun createParentViewHolder(
        container: ViewGroup,
        viewType: Int
    ): ParentViewHolder

    protected abstract fun createChildViewHolder(
        container: ViewGroup,
        viewType: Int
    ): ChildViewHolder

    fun toggle(index: Int) {
        require(!(index < 0 || index > dataProvider.getParentSize())) {
            "Cant expand the given index. Parent Size = " +
                    dataProvider.getParentSize() + ", index = " + index
        }
        val parentAdapterIndex = if (!adapterIndexConverter.isParentExpanded(index)) {
            expand(index)
        } else {
            collapse(index)
        }
        notifyItemChanged(
            parentAdapterIndex, if (adapterIndexConverter.isParentExpanded(index))
                EXPANDED
            else
                COLLAPSED
        )
    }

    /**
     * notifies the parent for only given [@param [parentDataIndex]]
     */
    fun notifyParent(parentDataIndex: Int) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex)
        notifyItemChanged(adapterParentIndex)
    }

    /**
     * notifies the parent for only given [@param [parentDataIndex]]
     *
     * @note choose a data type for [@param [payload]] other than [Integer]
     */
    fun notifyParent(parentDataIndex: Int, payload: Any?) {
        val adapterParentIndex = adapterIndexConverter.getParentAdapterIndex(parentDataIndex)
        notifyItemChanged(adapterParentIndex, payload)
    }


    /**
     * notifies the parent for only given [@param [childCoordinate]]
     */
    fun notifyChild(childCoordinate: ChildCoordinate) {
        val adapterParentIndex =
            adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex)
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1)
    }

    /**
     * notifies the parent for only given [@param [childCoordinate]]
     *
     * @note choose a data type for [@param [payload]] other than [Integer]
     */
    fun notifyChild(childCoordinate: ChildCoordinate, payload: Any?) {
        val adapterParentIndex =
            adapterIndexConverter.getParentAdapterIndex(childCoordinate.parentIndex)
        notifyItemChanged(adapterParentIndex + childCoordinate.childRelativeIndex + 1, payload)
    }

    /**
     * Method to externally flush index cache to handle parent/child matching.
     *
     * @note be warned to call this method carefully
     */
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
