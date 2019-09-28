package com.umut.petexpandable

import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider
import com.umut.petexpandable.model.MainStreamGenre
import com.umut.petexpandable.model.SubGenre

class SampleDataIndexProvider(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableDataIndexProvider {
    companion object {
        const val ROCK_VIEW_TYPE = 1
    }

    override fun getChildrenSize(parentIndex: Int): Int {
        return subGenres[mainStreamGenres[parentIndex]]?.size ?: 0
    }

    override fun getParentSize(): Int {
        return mainStreamGenres.size
    }

    override fun getParentViewType(parentIndex: Int): Int {
        if (mainStreamGenres[parentIndex].name == "ROCK") {
            return ROCK_VIEW_TYPE
        }
        return super.getParentViewType(parentIndex)
    }

    override fun getChildViewType(childCoordinate: ChildCoordinate): Int {
        val mainStreamGenre = mainStreamGenres[childCoordinate.parentIndex]
        if (mainStreamGenre.name == "ROCK"
            && subGenres[mainStreamGenre]?.get(childCoordinate.childRelativeIndex)?.name == "Grunge"
        ) {
            return ROCK_VIEW_TYPE
        }
        return super.getChildViewType(childCoordinate)
    }
}
