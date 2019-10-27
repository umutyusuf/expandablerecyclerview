package com.umut.expandablrecyclerview.holder

import android.view.View
import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.position.ItemPosition

abstract class ChildViewHolder<T : ListViewItem>(view: View) :
    ExpandableViewHolder(view) {

    override fun bind(position: ItemPosition, item: ListViewItem) {
        bindChild(position, item as T)
    }
    abstract fun bindChild(nestedPosition: ItemPosition, item: T)
}