package com.umut.expandablerecyclerview.item

/**
 * custom [ListViewItem] for convenience.
 */
interface ParentViewItem : ListViewItem {

    override fun isParent() = true
}