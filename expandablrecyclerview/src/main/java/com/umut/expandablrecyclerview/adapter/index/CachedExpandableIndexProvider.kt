package com.umut.expandablrecyclerview.adapter.index

import android.util.SparseArray
import android.util.SparseIntArray

import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.Companion.UNSPECIFIED

class CachedExpandableIndexProvider : ExpandableIndexProvider {

    private var totalItemCount: Int = 0
    private val parentPositionCache: SparseIntArray = SparseIntArray()
    private val childCoordinateCache: SparseArray<ChildCoordinate> = SparseArray()
    private val viewTypeCache: SparseIntArray = SparseIntArray()

    init {
        totalItemCount = UNSPECIFIED
    }

    override fun getTotalItemCount(): Int {
        return totalItemCount
    }

    override fun getAdapterIndexForParentAt(adapterParentIndex: Int): Int {
        throw UnsupportedOperationException("This operation is not supported.")
    }

    override fun getViewType(position: Int): Int {
        return viewTypeCache.get(position, UNSPECIFIED)
    }

    override fun parentIndexFromAdapterPosition(adapterPosition: Int): Int {
        return parentPositionCache.get(adapterPosition, UNSPECIFIED)
    }

    override fun isExpanded(dataPosition: Int): Boolean {
        throw UnsupportedOperationException("This operation is not supported")
    }

    override fun onExpand(dataPosition: Int) {
        throw UnsupportedOperationException("This operation is not supported")
    }

    override fun onCollapse(dataPosition: Int) {
        throw UnsupportedOperationException("This operation is not supported")
    }

    override fun getChildCoordinateFromAdapterIndex(adapterPosition: Int): ChildCoordinate? {
        return childCoordinateCache.get(adapterPosition)
    }

    internal fun flushCache() {
        totalItemCount = UNSPECIFIED
        parentPositionCache.clear()
        childCoordinateCache.clear()
        viewTypeCache.clear()
    }

    internal fun cacheParentPosition(position: Int, parentIndex: Int) {
        parentPositionCache.append(position, parentIndex)
    }

    internal fun cacheChildCoordinate(position: Int, coordinate: ChildCoordinate?) {
        childCoordinateCache.append(position, coordinate)
    }

    internal fun cacheTotalItemCount(totalItemCount: Int) {
        this.totalItemCount = totalItemCount
    }

    internal fun cacheViewType(position: Int, @ExpandableIndexProvider.ViewType viewType: Int) {
        this.viewTypeCache.append(position, viewType)
    }
}
