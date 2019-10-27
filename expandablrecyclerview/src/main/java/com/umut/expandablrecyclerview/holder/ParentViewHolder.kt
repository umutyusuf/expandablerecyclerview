package com.umut.expandablrecyclerview.holder

import android.view.View
import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.item.ParentViewItem
import com.umut.expandablrecyclerview.position.ItemPosition

abstract class ParentViewHolder<T : ParentViewItem>(view: View) :
    ExpandableViewHolder(view) {

    override fun bind(position: ItemPosition, item: ListViewItem) {
        bind(position, item as T)
    }

    abstract fun bind(position: ItemPosition, item: T)

    /**
     * Invoked when the parent actually toggled
     * You must perform view changes operations
     * for parent view item related to expand/collapse in this method
     */
    abstract fun onToggle(parentItem: ParentViewItem)
}