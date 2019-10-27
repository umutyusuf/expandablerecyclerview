package com.umut.petexpandable.model

import com.umut.expandablrecyclerview.item.ChildViewItem
import com.umut.expandablrecyclerview.item.ListViewItem

class SubGenre(val name: String) : ChildViewItem {

    override fun getItemType(): Int {
        return if (name == "Grunge") {
            MainStreamGenre.ROCK_VIEW_TYPE
        } else {
            super.getItemType()
        }
    }

    override fun areContentsSame(otherItem: ListViewItem): Boolean {
        if (otherItem !is SubGenre) {
            return false
        }
        return this.name == otherItem.name
    }

    override fun areItemsSame(otherItem: ListViewItem): Boolean {
        if (otherItem !is SubGenre) {
            return false
        }
        return this == otherItem
    }
}
