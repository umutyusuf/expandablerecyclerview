package com.umut.expandablrecyclerview.item

interface ChildViewItem : ListViewItem {
    override fun isParent() = false

    override fun isExpanded() = false

    override fun setExpanded(expanded: Boolean) {
        // ignore
    }
}