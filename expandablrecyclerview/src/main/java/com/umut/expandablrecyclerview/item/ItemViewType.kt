package com.umut.expandablrecyclerview.item

/**
 * Internal class that represents the view type more verbose to differentiate
 * parent/child views and externally defined view types
 */
internal data class ItemViewType(
    val isParent: Boolean,
    val externalViewType: Int
)