package com.umut.petexpandable

import com.umut.expandablerecyclerview.item.ListViewItem
import com.umut.expandablerecyclerview.provider.ExpandableDataSource
import com.umut.petexpandable.model.MainStreamGenre
import com.umut.petexpandable.model.SubGenre

class SampleDataIndexProvider(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableDataSource<SubGenre, MainStreamGenre> {

    override fun getItems(): Iterable<MainStreamGenre> {
        return mainStreamGenres
    }

    override fun <I : ListViewItem> getChildDataSource(listViewItem: ListViewItem): ExpandableDataSource<I, SubGenre>? {
        return object : ExpandableDataSource<I, SubGenre> {
            override fun getItems(): Iterable<SubGenre> {
                return subGenres[listViewItem as MainStreamGenre] ?: emptyList()
            }

            override fun <R : ListViewItem> getChildDataSource(
                listViewItem: ListViewItem
            ): ExpandableDataSource<R, I>? = null
        }
    }
}
