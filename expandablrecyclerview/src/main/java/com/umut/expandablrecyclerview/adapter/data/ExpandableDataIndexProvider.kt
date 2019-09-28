package com.umut.expandablrecyclerview.adapter.data

interface ExpandableDataIndexProvider {

    val parentSize: Int

    fun getChildrenSize(parentIndex: Int): Int

}
