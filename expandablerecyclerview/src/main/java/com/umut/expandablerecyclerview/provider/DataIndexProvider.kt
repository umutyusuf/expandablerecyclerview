package com.umut.expandablerecyclerview.provider

import androidx.recyclerview.widget.DiffUtil
import com.umut.expandablerecyclerview.item.ItemViewType
import com.umut.expandablerecyclerview.item.ListViewItem
import com.umut.expandablerecyclerview.item.ParentViewItem
import com.umut.expandablerecyclerview.position.ItemPosition

/**
 * Internal helper contract.
 * Contract to provide the following info
 *  -> provide correct [ItemViewType] by inverting the adapter position,
 *  -> respective [ListViewItem] for given position
 *  -> Size of the final data set (including all expanded/collapsed computations
 */
internal interface DataIndexProvider<in C : ListViewItem, in P : ParentViewItem> {

    /**
     * Updates the data source
     */
    fun updateDataSource(expandableDataSource: ExpandableDataSource<C, P>): DiffUtil.DiffResult

    /**
     * Returns [hashCode] for [ItemViewType]
     * @param adapterPosition: position of the view in adapter
     */
    fun getAdapterViewType(adapterPosition: Int): Int

    /**
     * Returned mapped [ItemViewType] for given [hashCodeOfViewType]
     * @param hashCodeOfViewType hashCode of [ItemViewType] previously provided by [getAdapterViewType]
     */
    fun getItemViewType(hashCodeOfViewType: Int): ItemViewType

    /**
     * * @param adapterPosition position of the view for the adapter
     * @return the corresponding [ListViewItem] for given adapter index
     */
    fun getListViewItem(adapterPosition: Int): ListViewItem

    /**
     * @return total item count
     */
    fun getItemCount(): Int

    /**
     * @param adapterPosition position of the view for the adapter
     * @return related [ListViewItem] and it's position extracted from [adapterPosition]
     */
    fun getItemAndPosition(adapterPosition: Int): Pair<ListViewItem, ItemPosition>

    /**
     * * @param parentIndex index of parent in data source
     * @return adapter position from [parentIndex]
     */
    fun getAdapterPositionFromParentIndex(parentIndex: Int): Int

    /**
     * @param itemPosition position of child in data source
     * @return adapter position from [itemPosition]
     */
    fun getAdapterPositionFromChildPosition(itemPosition: ItemPosition): Int

    /**
     * Expands the given item and updates the adapter positions and related mappings
     * @param parentViewItem index of parent in data source
     * @return a pair as
     * [Pair.first]     as  -> index of parent item for adapter
     * [Pair.second]    as  -> count of the child items for given parent item
     */
    fun expandParentAndGetAdapterPositionAndChildCount(parentViewItem: ListViewItem): Pair<Int, Int>

    /**
     * Collapse the given item and updates the adapter positions and related mappings
     * @param parentIndex index of parent in data source
     * @return a pair as
     * [Pair.first]     as  -> index of parent item for adapter
     * [Pair.second]    as  -> count of the child items for given parent item
     */
    fun collapseParentAndGetAdapterPositionAndChildCount(parentViewItem: ListViewItem): Pair<Int, Int>

    fun getAdapterPositionOfItem(listViewItem: ListViewItem): Int

    fun getParentItemFromDataIndex(parentIndex: Int): ListViewItem
    fun getAdapterIndexFromListViewItem(parentElement: ListViewItem): Int
}