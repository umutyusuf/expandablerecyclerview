package com.umut.expandablerecyclerview.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.umut.expandablerecyclerview.item.ListViewItem
import com.umut.expandablerecyclerview.position.ItemPosition

abstract class ExpandableViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {
    abstract fun bind(position: ItemPosition, item: ListViewItem)
}