package com.umut.expandablrecyclerview.adapter.data

import com.umut.expandablrecyclerview.adapter.ChildCoordinate

interface ExpandableDataIndexProvider {

    companion object {
        const val DEFAULT_VIEW_TYPE = 0
    }

    fun getParentSize(): Int

    fun getChildrenSize(parentIndex: Int): Int

    fun getParentViewType(parentIndex: Int) = DEFAULT_VIEW_TYPE

    fun getChildViewType(childCoordinate: ChildCoordinate) = DEFAULT_VIEW_TYPE


}
