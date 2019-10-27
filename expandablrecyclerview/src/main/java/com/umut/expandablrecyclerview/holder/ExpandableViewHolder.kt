package com.umut.expandablrecyclerview.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.position.ItemPosition

abstract class ExpandableViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(position: ItemPosition, item: ListViewItem)
}