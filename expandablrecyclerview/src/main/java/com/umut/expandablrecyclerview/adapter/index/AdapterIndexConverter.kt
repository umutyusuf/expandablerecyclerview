package com.umut.expandablrecyclerview.adapter.index


import android.annotation.SuppressLint
import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.Companion.UNSPECIFIED

import com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.ViewType

class AdapterIndexConverter(private val defaultIndexProvider: ExpandableIndexProvider) {

    private val cachedIndexProvider: CachedExpandableIndexProvider = CachedExpandableIndexProvider()

    fun isParentExpanded(dataPosition: Int): Boolean {
        return defaultIndexProvider.isExpanded(dataPosition)
    }

    fun expandParent(dataPosition: Int) {
        defaultIndexProvider.onExpand(dataPosition)
        flushCache()
    }

    fun collapseParent(dataPosition: Int) {
        defaultIndexProvider.onCollapse(dataPosition)
        flushCache()
    }

    fun calculateTotalItemCount(): Int {
        if (cachedIndexProvider.getTotalItemCount() != UNSPECIFIED) {
            return cachedIndexProvider.getTotalItemCount()
        }
        val totalItemCount = defaultIndexProvider.getTotalItemCount()
        cachedIndexProvider.cacheTotalItemCount(totalItemCount)
        return totalItemCount
    }

    fun getParentAdapterIndex(index: Int): Int {
        return defaultIndexProvider.getAdapterIndexForParentAt(index)
    }

    @SuppressLint("WrongConstant")
    @ViewType
    fun findViewType(index: Int): Int {
        if (cachedIndexProvider.getViewType(index) != UNSPECIFIED) {
            return cachedIndexProvider.getViewType(index)
        }
        val viewType = defaultIndexProvider.getViewType(index)
        cachedIndexProvider.cacheViewType(index, viewType)
        return viewType
    }

    fun getParentPosition(index: Int): Int {
        if (cachedIndexProvider.parentIndexFromAdapterPosition(index) != UNSPECIFIED) {
            return cachedIndexProvider.parentIndexFromAdapterPosition(index)
        }
        val parentPosition = defaultIndexProvider.parentIndexFromAdapterPosition(index)
        cachedIndexProvider.cacheParentPosition(index, parentPosition)
        return parentPosition
    }

    fun getChildCoordinate(index: Int): ChildCoordinate? {
        if (cachedIndexProvider.getChildCoordinateFromAdapterIndex(index) != null) {
            return cachedIndexProvider.getChildCoordinateFromAdapterIndex(index)
        }
        val childCoordinate = defaultIndexProvider
                .getChildCoordinateFromAdapterIndex(index)
        cachedIndexProvider.cacheChildCoordinate(index, childCoordinate)
        return childCoordinate
    }

    fun flushCache() {
        cachedIndexProvider.flushCache()
    }
}
