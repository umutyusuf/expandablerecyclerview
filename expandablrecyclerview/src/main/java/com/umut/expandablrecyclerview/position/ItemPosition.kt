package com.umut.expandablrecyclerview.position

import com.umut.expandablrecyclerview.item.ListViewItem

data class ItemPosition(
    val parent: ListViewItem?,
    val childIndex: Int
)