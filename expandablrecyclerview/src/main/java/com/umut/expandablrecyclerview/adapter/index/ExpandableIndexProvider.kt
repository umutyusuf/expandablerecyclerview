package com.umut.expandablrecyclerview.adapter.index

import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.holder.ViewType

interface ExpandableIndexProvider {
    companion object {
        const val UNSPECIFIED = -1
    }

    fun getTotalItemCount(): Int

    fun getAdapterIndexForParentAt(adapterParentIndex: Int): Int

    /**
     * @param position position of the view
     * @return  [ViewType] object containing if it's a parent and externally provided viewType
     */
    fun getViewTypeForPosition(position: Int): ViewType?

    fun parentIndexFromAdapterPosition(adapterPosition: Int): Int

    fun isExpanded(dataPosition: Int): Boolean

    fun onExpand(dataPosition: Int)

    fun onCollapse(dataPosition: Int)

    fun getChildCoordinateFromAdapterIndex(adapterPosition: Int): ChildCoordinate?

}
