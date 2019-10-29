package com.umut.expandablerecyclerview

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.umut.expandablerecyclerview.holder.ChildViewHolder
import com.umut.expandablerecyclerview.holder.ExpandableViewHolder
import com.umut.expandablerecyclerview.holder.ParentViewHolder
import com.umut.expandablerecyclerview.item.ListViewItem
import com.umut.expandablerecyclerview.item.ParentViewItem
import com.umut.expandablerecyclerview.position.ItemPosition
import com.umut.expandablerecyclerview.provider.ExpandableDataSource
import com.umut.expandablerecyclerview.provider.impl.DataIndexProviderImpl

/**
 * [RecyclerView.Adapter] replacement for expandable implementation.
 * This class provides all the convenience to implement expand/collapse mechanism for [RecyclerView]
 */
abstract class ExpandableRecyclerViewAdapter<C : ListViewItem, P : ParentViewItem> :
    RecyclerView.Adapter<ExpandableViewHolder>() {

    companion object {
        private const val TAG = "ExpandableAdapter"
    }

    private val positionIndexProvider = DataIndexProviderImpl<C, P>()

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ExpandableViewHolder {
        val itemViewType = positionIndexProvider.getItemViewType(viewType)
        if (itemViewType.isParent) {
            return createParentViewHolder(parent, itemViewType.externalViewType)
        }
        return createChildViewHolder(parent, itemViewType.externalViewType)
    }

    final override fun onBindViewHolder(
        holder: ExpandableViewHolder,
        position: Int
    ) {
        val itemAndPosition = positionIndexProvider.getItemAndPosition(position)
        holder.bind(itemAndPosition.second, itemAndPosition.first)
    }

    @CallSuper
    override fun onBindViewHolder(
        holder: ExpandableViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        payloads.takeIf {
            it.isNotEmpty() && it[0] is ParentItemTogglePayload
        }?.apply {
            val parentViewHolder = holder as ParentViewHolder<*>
            val parentViewItem = positionIndexProvider.getListViewItem(position) as ParentViewItem
            parentViewHolder.onToggle(parentViewItem)

        } ?: super.onBindViewHolder(holder, position, payloads)
    }

    override fun getItemCount() = positionIndexProvider.getItemCount()

    override fun getItemViewType(position: Int) = positionIndexProvider.getAdapterViewType(position)

    // endregion [RecyclerView.Adapter methods]

    // region [Abstract methods]
    abstract fun createParentViewHolder(container: ViewGroup, viewType: Int): ParentViewHolder<P>

    abstract fun createChildViewHolder(container: ViewGroup, viewType: Int): ChildViewHolder<C>
    // endregion [Abstract methods]

    // region [Public API]
    fun updateDataSource(expandableDataSource: ExpandableDataSource<C, P>) {
        val diffResult = positionIndexProvider.updateDataSource(expandableDataSource)
        diffResult.dispatchUpdatesTo(this)

    }

    fun expand(listViewItem: ListViewItem) {
        require(listViewItem.isParent()) { "Given item must be parent!" }
        doExpand(listViewItem)
    }

    fun expand(parentIndex: Int) {
        val listViewItem = positionIndexProvider.getParentItemFromDataIndex(parentIndex)
        require(listViewItem.isParent()) { "ListViewItem must be parent" }
        if (listViewItem.isExpanded()) {
            Log.i(TAG, "Parent already expanded. Parent position $parentIndex")
            return
        }
        doExpand(listViewItem)
    }

    fun collapse(listViewItem: ListViewItem) {
        require(listViewItem.isParent()) { "Given item must be parent!" }
        doCollapse(listViewItem)
    }

    fun collapse(parentIndex: Int) {
        val listViewItem = positionIndexProvider.getParentItemFromDataIndex(parentIndex)
        require(listViewItem.isParent()) { "ListViewItem must be parent" }
        if (!listViewItem.isExpanded()) {
            Log.i(TAG, "Parent already collapsed. Parent position $parentIndex")
            return
        }
        doCollapse(listViewItem)
    }

    /**
     * notifies the parent to bind. Implementation will invoke
     * [androidx.recyclerview.widget.RecyclerView.Adapter.notifyItemChanged] with proper index.
     * Support for nested structure
     *
     * @param listViewItem list view item
     */
    fun notifyParent(listViewItem: ListViewItem) {
        notifyParent(listViewItem, null)
    }

    /**
     * notifies the parent to bind with payload
     *[androidx.recyclerview.widget.RecyclerView.Adapter.notifyItemChanged] with proper index and payload
     * Support for nested structure
     *
     * @param listViewItem list view item
     * @param payload to notify with
     */
    fun notifyParent(listViewItem: ListViewItem, payload: Any?) {
        notifyItemChanged(
            positionIndexProvider.getAdapterPositionOfItem(listViewItem),
            payload
        )
    }


    /**
     * notifies the parent to bind. Implementation will invoke
     * [androidx.recyclerview.widget.RecyclerView.Adapter.notifyItemChanged] with proper index.
     *
     * @param parentDataIndex index of parent
     */
    fun notifyParent(parentDataIndex: Int) {
        notifyParent(parentDataIndex, null)
    }

    /**
     * notifies the parent to bind with payload
     *[androidx.recyclerview.widget.RecyclerView.Adapter.notifyItemChanged] with proper index and payload
     * @param parentDataIndex index of parent
     * @param payload to notify with
     */
    fun notifyParent(parentDataIndex: Int, payload: Any?) {
        notifyItemChanged(
            positionIndexProvider.getAdapterPositionFromParentIndex(parentDataIndex),
            payload
        )
    }

    /**
     * notifies the child to bind
     * @param nestedPosition
     */
    fun notifyChild(nestedPosition: ItemPosition) {
        notifyChild(nestedPosition, null)
    }

    /**
     * notifies the child to bind
     * @param nestedPosition
     */
    fun notifyChild(nestedPosition: ItemPosition, payload: Any?) {
        notifyItemChanged(
            positionIndexProvider.getAdapterPositionFromChildPosition(nestedPosition),
            payload
        )
    }

    /**
     * Notifies the holder associated with given item
     */
    fun notifyItem(listViewItem: ListViewItem) {
        notifyItem(listViewItem, null)
    }

    /**
     * Notifies the holder associated with given item
     */
    fun notifyItem(listViewItem: ListViewItem, payload: Any?) {
        val adapterIndex = positionIndexProvider.getAdapterIndexFromListViewItem(listViewItem)
        notifyItemChanged(adapterIndex, payload)
    }

    // endregion [Public API]

    // region [Private Methods]
    private fun doExpand(listViewItem: ListViewItem) {
        val parentAdapterIndexAndChildCount =
            positionIndexProvider.expandParentAndGetAdapterPositionAndChildCount(listViewItem)
        notifyItemRangeInserted(
            parentAdapterIndexAndChildCount.first + 1,
            parentAdapterIndexAndChildCount.second
        )
        notifyItemChanged(parentAdapterIndexAndChildCount.first, ParentItemTogglePayload(true))
    }

    private fun doCollapse(listViewItem: ListViewItem) {
        val parentAdapterIndexAndChildCount =
            positionIndexProvider.collapseParentAndGetAdapterPositionAndChildCount(listViewItem)
        notifyItemRangeRemoved(
            parentAdapterIndexAndChildCount.first + 1,
            parentAdapterIndexAndChildCount.second
        )
        notifyItemChanged(parentAdapterIndexAndChildCount.first, ParentItemTogglePayload())
    }
    // endregion [Private Methods]
}