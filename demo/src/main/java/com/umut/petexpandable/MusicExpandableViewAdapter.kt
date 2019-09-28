package com.umut.petexpandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView

import com.umut.expandablrecyclerview.adapter.ChildCoordinate
import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter
import com.umut.expandablrecyclerview.adapter.holder.ChildViewHolder
import com.umut.expandablrecyclerview.adapter.holder.ParentViewHolder
import com.umut.petexpandable.model.MainStreamGenre
import com.umut.petexpandable.model.SubGenre

class MusicExpandableViewAdapter(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableViewAdapter(SampleDataIndexProvider(mainStreamGenres, subGenres)) {

    override fun createParentViewHolder(container: ViewGroup): MainStreamViewHolder {
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_main_stream,
            container, false
        )
        return MainStreamViewHolder(view)
    }

    override fun createChildViewHolder(container: ViewGroup): SubGenreViewHolder {
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_sub_genre,
            container, false
        )
        return SubGenreViewHolder(view)
    }

    inner class MainStreamViewHolder(itemView: View) : ParentViewHolder(itemView) {

        private val nameTextView: TextView =
            itemView.findViewById(R.id.text_view_main_stream_genre_name)
        private val expandIndicatorImageView: AppCompatImageView =
            itemView.findViewById(R.id.image_view_expand_indicator)

        override fun bind(coordinate: Int, state: Int) {
            itemView.setOnClickListener { toggle(coordinate) }
            nameTextView.text = mainStreamGenres[coordinate].name
            setState(state == State.EXPANDED)
        }

        private fun setState(expand: Boolean) {
            if (!expand) {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_down)
            } else {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_up)
            }
        }

    }

    inner class SubGenreViewHolder internal constructor(itemView: View) :
        ChildViewHolder(itemView) {


        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_sub_genre_name)

        override fun bind(coordinate: ChildCoordinate) {
            nameTextView.text = subGenres[mainStreamGenres[coordinate.parentIndex]]
                ?.get(coordinate.childRelativeIndex)
                ?.name ?: ""
        }

    }
}
