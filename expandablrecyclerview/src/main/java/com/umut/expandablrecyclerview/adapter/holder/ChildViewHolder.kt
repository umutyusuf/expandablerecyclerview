package com.umut.expandablrecyclerview.adapter.holder

import android.view.View

import com.umut.expandablrecyclerview.adapter.ChildCoordinate

abstract class ChildViewHolder(itemView: View) : ExpandableViewHolder<ChildCoordinate>(itemView) {

    /**
     * binds the value to view holder
     *
     * @param coordinate Coordinate of item
     */
    abstract fun bind(coordinate: ChildCoordinate)

    fun update(payload: List<Any>, coordinate: ChildCoordinate): Boolean {
        return false
    }
}
