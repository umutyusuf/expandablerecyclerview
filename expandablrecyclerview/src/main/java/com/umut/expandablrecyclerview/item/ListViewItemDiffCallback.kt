package com.umut.expandablrecyclerview.item

import androidx.recyclerview.widget.DiffUtil

class ListViewItemDiffCallback(
    private val previousListOfItems: List<ListViewItem>,
    private val updatedListOfItems: List<ListViewItem>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return previousListOfItems[oldItemPosition]
            .areItemsSame(updatedListOfItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return previousListOfItems[oldItemPosition]
            .areContentsSame(updatedListOfItems[newItemPosition])
    }

    override fun getOldListSize() = previousListOfItems.size

    override fun getNewListSize() = updatedListOfItems.size
}