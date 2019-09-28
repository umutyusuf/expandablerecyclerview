package com.umut.expandablrecyclerview.adapter.holder


import android.view.View

import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter.State

abstract class ParentViewHolder(itemView: View) : ExpandableViewHolder<Int>(itemView) {

    /**
     * binds the value to view holder
     *
     * @param coordinate Coordinate of item
     * @param state      state of parent (expanded or collapsed)
     */
    abstract fun bind(coordinate: Int, @State state: Int)

    fun onStateChanged(coordinate: Int, @State state: Int): Boolean {
        return false
    }

    fun update(payload: List<Any>, coordinate: Int, @State state: Int): Boolean {
        return false
    }
}
