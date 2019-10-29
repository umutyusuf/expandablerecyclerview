package com.umut.petexpandable

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.umut.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.umut.expandablerecyclerview.holder.ChildViewHolder
import com.umut.expandablerecyclerview.holder.ParentViewHolder
import com.umut.expandablerecyclerview.item.ParentViewItem
import com.umut.expandablerecyclerview.position.ItemPosition
import com.umut.petexpandable.model.MainStreamGenre
import com.umut.petexpandable.model.MainStreamGenre.Companion.ROCK_VIEW_TYPE
import com.umut.petexpandable.model.SubGenre

class MusicExpandableViewAdapter(
    private val mainStreamGenres: List<MainStreamGenre>,
    private val subGenres: Map<MainStreamGenre, List<SubGenre>>
) : ExpandableRecyclerViewAdapter<SubGenre, MainStreamGenre>() {

    override fun createParentViewHolder(container: ViewGroup, viewType: Int): MainStreamViewHolder {
        if (viewType == ROCK_VIEW_TYPE) {
            val view = LayoutInflater.from(container.context).inflate(
                R.layout.item_main_stream_red,
                container, false
            )
            return MainStreamViewHolder(view)
        }
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_main_stream,
            container, false
        )
        return MainStreamViewHolder(view)
    }

    override fun createChildViewHolder(container: ViewGroup, viewType: Int): SubGenreViewHolder {
        if (viewType == ROCK_VIEW_TYPE) {
            val view = LayoutInflater.from(container.context).inflate(
                R.layout.item_sub_genre_red,
                container, false
            )
            return SubGenreViewHolder(view)
        }
        val view = LayoutInflater.from(container.context).inflate(
            R.layout.item_sub_genre,
            container, false
        )
        return SubGenreViewHolder(view)
    }

    inner class MainStreamViewHolder(private val view: View) :
        ParentViewHolder<MainStreamGenre>(view) {

        override fun bind(position: ItemPosition, item: MainStreamGenre) {
            view.setOnClickListener {
                if (!item.isExpanded()) {
                    expand(position.childIndex)
                } else {
                    collapse(position.childIndex)
                }
            }
            nameTextView.text = mainStreamGenres[position.childIndex].name
            setState(item.isExpanded())
        }

        override fun onToggle(parentItem: ParentViewItem) {
            setState(parentItem.isExpanded())
        }

        private val nameTextView: TextView =
            itemView.findViewById(R.id.text_view_main_stream_genre_name)
        private val expandIndicatorImageView: AppCompatImageView =
            itemView.findViewById(R.id.image_view_expand_indicator)


        private fun setState(expand: Boolean) {
            if (!expand) {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_down)
            } else {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_up)
            }
        }

    }

    inner class SubGenreViewHolder internal constructor(itemView: View) :
        ChildViewHolder<SubGenre>(itemView) {
        override fun bindChild(
            nestedPosition: ItemPosition,
            item: SubGenre
        ) {
            nameTextView.text = item.name
        }


        private val nameTextView: TextView = itemView.findViewById(R.id.text_view_sub_genre_name)

    }
}
