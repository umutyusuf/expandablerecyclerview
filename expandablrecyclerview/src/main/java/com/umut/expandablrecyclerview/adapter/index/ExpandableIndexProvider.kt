package com.umut.expandablrecyclerview.adapter.index

import androidx.annotation.IntDef
import com.umut.expandablrecyclerview.adapter.ChildCoordinate

interface ExpandableIndexProvider {
    companion object {
        const val PARENT = 0
        const val CHILD = 1

        const val UNSPECIFIED = -1
    }

    fun getTotalItemCount(): Int

    fun getAdapterIndexForParentAt(adapterParentIndex: Int): Int

    @ViewType
    fun getViewType(position: Int): Int

    fun parentIndexFromAdapterPosition(adapterPosition: Int): Int

    fun isExpanded(dataPosition: Int): Boolean

    fun onExpand(dataPosition: Int)

    fun onCollapse(dataPosition: Int)

    fun getChildCoordinateFromAdapterIndex(adapterPosition: Int): ChildCoordinate?

    @IntDef(PARENT, CHILD)
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class ViewType
}
