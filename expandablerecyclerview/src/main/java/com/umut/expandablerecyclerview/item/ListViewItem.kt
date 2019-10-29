package com.umut.expandablerecyclerview.item

interface ListViewItem {
    companion object {
        private const val DEFAULT_VIEW_TYPE = 0
    }
    /**
     * @return true if item is expanded, false otherwise
     * @noteThis method is available to provide convenience on initial data load
     */
    fun isExpanded(): Boolean

    /**
     * Updates expanded state of item
     */
    fun setExpanded(expanded: Boolean)

    /**
     * @return custom view type for item.
     */
    fun getItemType() : Int = DEFAULT_VIEW_TYPE

    /**
     * @return true if the item is parent, false otherwise.
     * @note for convenience regarding basic scenarios use [ChildViewItem] and [ParentViewItem]
     */
    fun isParent() : Boolean

    /**
     * Method to be utilised with [androidx.recyclerview.widget.DiffUtil.Callback] implementation
     * @see [androidx.recyclerview.widget.DiffUtil.Callback.areContentsTheSame]
     */
    fun areContentsSame(otherItem: ListViewItem): Boolean

    /**
     * Method to be utilised with [androidx.recyclerview.widget.DiffUtil.Callback] implementation
     * @see [androidx.recyclerview.widget.DiffUtil.Callback.areItemsTheSame]
     */
    fun areItemsSame(otherItem: ListViewItem): Boolean
}