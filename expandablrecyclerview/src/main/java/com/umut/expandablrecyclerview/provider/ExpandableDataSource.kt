package com.umut.expandablrecyclerview.provider

import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.item.ParentViewItem

/**
 * Contract for providing data for [com.umut.expandablrecyclerview.ExpandableRecyclerViewAdapter]
 * @param P type of items
 * @param C type of items in child Data source
 */
interface ExpandableDataSource<out C : ListViewItem, out P : ListViewItem> {

    /**
     * Returns the available items
     */
    fun getItems(): Iterable<P>

    /**
     * @return child items for given view.
     * @note This method resides here to support the nested section mechanism
     * for instance
     *  parent1
     *      parent2
     *          child
     *          child
     *      parent2
     *          child
     *  parent1
     */
    fun <I : ListViewItem> getChildDataSource(listViewItem: ListViewItem): ExpandableDataSource<I, C>?

}