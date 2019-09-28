package com.umut.expandablrecyclerview.adapter.index


import android.util.SparseArray
import androidx.core.util.set
import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.holder.ViewType
import com.umut.expandablrecyclerview.adapter.index.ExpandableIndexProvider.Companion.UNSPECIFIED

class AdapterIndexConverter(private val defaultIndexProvider: ExpandableIndexProvider) {

    private val cachedIndexProvider: CachedExpandableIndexProvider = CachedExpandableIndexProvider()
    private val viewTypeMap = SparseArray<ViewType>()

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

    fun findViewType(index: Int): Int {
        val cachedViewType = cachedIndexProvider.getViewTypeForPosition(index)
        if (cachedViewType != null) {
            return cachedViewType.hashCode()
        }
        return defaultIndexProvider.getViewTypeForPosition(index)?.let { viewType ->
            cachedIndexProvider.cacheViewType(index, viewType)
            val viewTypeHashCode = viewType.hashCode()
            viewTypeMap[viewTypeHashCode] = viewType
            return@let viewTypeHashCode
        } ?: 0
    }

    /**
     * @param viewType computed view type for the view to be created
     * @return [ViewType] object for given viewType
     */
    fun extractViewTypeDetail(viewType: Int): ViewType = viewTypeMap[viewType]


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
