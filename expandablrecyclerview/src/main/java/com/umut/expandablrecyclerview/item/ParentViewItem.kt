package com.umut.expandablrecyclerview.item

/**
 * custom [ListViewItem] for convenience.
 */
interface ParentViewItem : ListViewItem {

    override fun isParent() = true
}