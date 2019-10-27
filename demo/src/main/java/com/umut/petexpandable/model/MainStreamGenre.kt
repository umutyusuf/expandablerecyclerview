package com.umut.petexpandable.model

import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.item.ParentViewItem

class MainStreamGenre(val name: String) : ParentViewItem {

    private val fieldForTrick: Long = System.nanoTime()

    companion object {
        const val ROCK_VIEW_TYPE = 1
    }

    override fun getItemType(): Int {
        return if (name == "ROCK") {
            ROCK_VIEW_TYPE
        } else {
            super.getItemType()
        }
    }
    private var expanded : Boolean = false

    override fun setExpanded(expanded: Boolean) {
        this.expanded = expanded
    }

    override fun isExpanded() = expanded

    override fun areContentsSame(otherItem: ListViewItem): Boolean {
        if (otherItem !is MainStreamGenre) {
            return false
        }
        return this.name == otherItem.name
    }

    override fun areItemsSame(otherItem: ListViewItem): Boolean {
        return this == otherItem
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val genre = other as MainStreamGenre?

        return fieldForTrick == genre!!.fieldForTrick
    }

    override fun hashCode(): Int {
        return (fieldForTrick xor fieldForTrick.ushr(32)).toInt()
    }
}
