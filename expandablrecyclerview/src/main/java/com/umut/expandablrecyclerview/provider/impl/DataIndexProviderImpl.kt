package com.umut.expandablrecyclerview.provider.impl

import android.util.SparseArray
import androidx.core.util.set
import androidx.recyclerview.widget.DiffUtil
import com.umut.expandablrecyclerview.item.ItemViewType
import com.umut.expandablrecyclerview.item.ListViewItem
import com.umut.expandablrecyclerview.item.ListViewItemDiffCallback
import com.umut.expandablrecyclerview.item.ParentViewItem
import com.umut.expandablrecyclerview.position.ItemPosition
import com.umut.expandablrecyclerview.provider.DataIndexProvider
import com.umut.expandablrecyclerview.provider.ExpandableDataSource

internal class DataIndexProviderImpl<in C : ListViewItem, in P : ParentViewItem> :
    DataIndexProvider<C, P> {

    companion object {
        private const val TAG = "DataIndexProviderImpl"
    }

    private var expandableDataSource: ExpandableDataSource<C, P>? = null
    private val viewTypeHashToViewTypeMap = SparseArray<ItemViewType>()
    private val dataSourceSet = HashSet<ExpandableDataSource<ListViewItem, ListViewItem>>()
    private val itemList = ArrayList<ListViewItem>()

    override fun updateDataSource(expandableDataSource: ExpandableDataSource<C, P>): DiffUtil.DiffResult {
        val updatedList = generateListFromDataSource(expandableDataSource)
        val diffResult = DiffUtil.calculateDiff(ListViewItemDiffCallback(itemList, updatedList))
        this.itemList.clear()
        this.itemList.addAll(updatedList)
        this.expandableDataSource = expandableDataSource
        dataSourceSet.clear()
        updateDataSourceMap(expandableDataSource)
        return diffResult
    }


    override fun getAdapterViewType(adapterPosition: Int): Int {
        val listViewItem = itemList[adapterPosition]
        val itemViewType = ItemViewType(listViewItem.isParent(), listViewItem.getItemType())
        val hashCode = itemViewType.hashCode()
        viewTypeHashToViewTypeMap[hashCode] = itemViewType
        return hashCode
    }

    override fun getItemViewType(hashCodeOfViewType: Int): ItemViewType {
        return viewTypeHashToViewTypeMap[hashCodeOfViewType]
            ?: throw IllegalStateException("$TAG Error deciding the type of view")
    }

    override fun getListViewItem(adapterPosition: Int): ListViewItem = itemList[adapterPosition]

    override fun getItemCount(): Int = itemList.size

    override fun getItemAndPosition(adapterPosition: Int): Pair<ListViewItem, ItemPosition> {
        val listViewItem = itemList[adapterPosition]
        // check if item is root parent
        val indexOfRootParent = expandableDataSource!!.getItems().indexOf(listViewItem)
        if (indexOfRootParent < 0) {
            // means the item is nested item
            // in this case we go back from original list (this#itemList) to find the first parent
            var index = adapterPosition
            do {
                index--
            } while (!itemList[index].isParent())
            require(index >= 0) { "Can't find the child coordinate" }
            return Pair(
                listViewItem,
                ItemPosition(itemList[index], adapterPosition - index - 1)
            )
        }

        return Pair(listViewItem, ItemPosition(null, indexOfRootParent))
    }

    override fun getAdapterPositionFromParentIndex(parentIndex: Int): Int {
        val parentElement = getParentItemFromDataIndex(parentIndex)
        return getAdapterIndexFromListViewItem(parentElement)
    }

    override fun getParentItemFromDataIndex(parentIndex: Int): ListViewItem {
        return expandableDataSource!!.getItems().elementAt(parentIndex)
    }

    override fun getAdapterPositionFromChildPosition(itemPosition: ItemPosition): Int {
        return itemList.indexOf(itemPosition.parent) + itemPosition.childIndex
    }

    override fun expandParentAndGetAdapterPositionAndChildCount(parentViewItem: ListViewItem): Pair<Int, Int> {
        val parentItemPosition = getAdapterIndexFromListViewItem(parentViewItem)
        val childItems = getChildItemsFromParent(parentViewItem)
        itemList.addAll(parentItemPosition + 1, childItems)
        parentViewItem.setExpanded(true)
        return Pair(parentItemPosition, childItems.count())
    }

    override fun collapseParentAndGetAdapterPositionAndChildCount(parentViewItem: ListViewItem): Pair<Int, Int> {
        val parentItemPosition = getAdapterIndexFromListViewItem(parentViewItem)
        val childItems = getChildItemsFromParent(parentViewItem)
        itemList.removeAll(childItems)
        parentViewItem.setExpanded(false)
        return Pair(parentItemPosition, childItems.count())
    }

    override fun getAdapterPositionOfItem(listViewItem: ListViewItem): Int {
        return getAdapterIndexFromListViewItem(listViewItem)
    }

    override fun getAdapterIndexFromListViewItem(parentElement: ListViewItem) =
        itemList.indexOf(parentElement)

    // region [Private Methods]

    private fun <I : ListViewItem, R : ListViewItem> updateDataSourceMap(
        rootDataSource: ExpandableDataSource<I, R>?
    ) {
        if (rootDataSource != null) {
            dataSourceSet.add(rootDataSource)
            rootDataSource.getItems().forEach { item ->
                updateDataSourceMap<ListViewItem, I>(rootDataSource.getChildDataSource(item))
            }
        }

    }

    private fun <I : ListViewItem, R : ListViewItem> generateListFromDataSource(
        dataSource: ExpandableDataSource<I, R>
    ): List<ListViewItem> {
        val listOfItems = ArrayList<ListViewItem>()
        dataSource.getItems().forEach { parentItem ->
            listOfItems.add(parentItem)
            addItemRecursive<ListViewItem, I>(
                dataSource.getChildDataSource(parentItem),
                listOfItems
            )
        }

        return listOfItems
    }

    private fun <I : ListViewItem, R : ListViewItem> addItemRecursive(
        dataSource: ExpandableDataSource<I, R>?,
        listOfItems: ArrayList<ListViewItem>
    ) {
        dataSource?.takeIf { it.getItems().toList().isNotEmpty() }?.getItems()?.forEach { item ->
            if (item.isParent() && item.isExpanded()) {
                listOfItems.add(item)
                addItemRecursive<ListViewItem, I>(dataSource.getChildDataSource(item), listOfItems)
            }
        }
    }

    private fun getChildItemsFromParent(parentViewItem: ListViewItem): List<ListViewItem> {
        dataSourceSet.forEach { dataSource ->
            if (dataSource.getItems().indexOf(parentViewItem) >= 0) {
                val childDataSource = dataSource.getChildDataSource<ListViewItem>(parentViewItem)
                    ?: return emptyList()
                return generateListFromDataSource(childDataSource)
            }
        }
        throw IllegalStateException("Can't find data source related to parentView $parentViewItem")
    }

    // endregion [Private Methods]
}
