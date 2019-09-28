package com.umut.expandablrecyclerview.adapter.index

import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider
import com.umut.expandablrecyclerview.adapter.holder.ViewType
import java.util.*

class ComputingExpandableIndexProvider(private val dataProvider: ExpandableDataIndexProvider) :
    ExpandableIndexProvider {


    private val expandedParents = HashSet<Int>()

    override fun getTotalItemCount(): Int {
        var totalCount = dataProvider.getParentSize()
        for (parent in expandedParents) {
            totalCount += dataProvider.getChildrenSize(parent)
        }
        return totalCount
    }

    override fun getAdapterIndexForParentAt(adapterParentIndex: Int): Int {
        var currentCount = adapterParentIndex
        for (i in 0 until adapterParentIndex) {
            if (expandedParents.contains(i)) {
                currentCount += dataProvider.getChildrenSize(i)
            }
        }
        return currentCount
    }

    override fun getViewTypeForPosition(position: Int): ViewType {
        var nextParentIndex = 0
        for (i in 0 until dataProvider.getParentSize()) {
            if (position == nextParentIndex) {
                return ViewType(dataProvider.getParentViewType(i), true)
            }
            nextParentIndex++
            if (expandedParents.contains(i)) {
                nextParentIndex += dataProvider.getChildrenSize(i)
            }
        }
        return ViewType(
            dataProvider.getChildViewType(getChildCoordinateFromAdapterIndex(position)!!),
            false
        )
    }

    override fun parentIndexFromAdapterPosition(adapterPosition: Int): Int {
        var nextParentIndex = 0
        for (i in 0 until dataProvider.getParentSize()) {
            if (adapterPosition == nextParentIndex) {
                return i
            }
            nextParentIndex++
            if (expandedParents.contains(i)) {
                nextParentIndex += dataProvider.getChildrenSize(i)
            }
        }
        throw IllegalStateException("Parent index can not be computed")
    }

    override fun isExpanded(dataPosition: Int): Boolean {
        return expandedParents.contains(dataPosition)
    }

    override fun onExpand(dataPosition: Int) {
        this.expandedParents.add(dataPosition)
    }

    override fun onCollapse(dataPosition: Int) {
        this.expandedParents.remove(dataPosition)
    }

    override fun getChildCoordinateFromAdapterIndex(adapterPosition: Int): ChildCoordinate? {
        var totalItemCount = 0
        for (i in 0 until dataProvider.getParentSize()) {
            if (adapterPosition < totalItemCount) {
                return getChildCoordinate(i - 1, adapterPosition, totalItemCount)
            }
            totalItemCount++
            totalItemCount += if (expandedParents.contains(i)) dataProvider.getChildrenSize(i) else 0
        }
        if (adapterPosition < totalItemCount) {
            try {
                return getChildCoordinate(
                    dataProvider.getParentSize() - 1, adapterPosition,
                    totalItemCount
                )
            } catch (e: Exception) {
                throw IllegalStateException("Child Coordinate can not be computed", e)
            }

        }
        throw IllegalStateException("Child Coordinate can not be computed")
    }

    private fun getChildCoordinate(
        parentIndex: Int, adapterPosition: Int,
        markedItemCount: Int
    ): ChildCoordinate {
        val childPositionInParent =
            adapterPosition - (markedItemCount - dataProvider.getChildrenSize(parentIndex))
        return ChildCoordinate(parentIndex, childPositionInParent)
    }
}
