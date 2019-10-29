package com.umut.expandablerecyclerview.position

import com.umut.expandablerecyclerview.item.ListViewItem

data class ItemPosition(
    val parent: ListViewItem?,
    val childIndex: Int
)