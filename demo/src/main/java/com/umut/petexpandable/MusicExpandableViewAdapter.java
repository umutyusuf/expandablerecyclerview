package com.umut.petexpandable;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;
import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter;
import com.umut.expandablrecyclerview.adapter.holder.ChildViewHolder;
import com.umut.expandablrecyclerview.adapter.holder.ParentViewHolder;
import com.umut.petexpandable.model.MainStreamGenre;
import com.umut.petexpandable.model.SubGenre;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MusicExpandableViewAdapter extends ExpandableViewAdapter {

    @NonNull
    private final List<MainStreamGenre> mainStreamGenres;
    @NonNull
    private final Map<MainStreamGenre, List<SubGenre>> subGenres;

    @NonNull
    private final Set<Integer> expandedParents;

    public MusicExpandableViewAdapter(@NonNull List<MainStreamGenre> mainStreamGenres,
                                      @NonNull Map<MainStreamGenre, List<SubGenre>> subGenres) {
        super(new SampleDataIndexProvider(mainStreamGenres, subGenres));
        this.mainStreamGenres = mainStreamGenres;
        this.subGenres = subGenres;
        this.expandedParents = new HashSet<>();
    }

    @Override
    protected MainStreamViewHolder createParentViewHolder(@NonNull ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_main_stream,
                container, false);
        return new MainStreamViewHolder(view);
    }

    @Override
    protected SubGenreViewHolder createChildViewHolder(@NonNull ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_sub_genre,
                container, false);
        return new SubGenreViewHolder(view);
    }

    private class MainStreamViewHolder extends ParentViewHolder {

        private final TextView nameTextView;
        private final AppCompatImageView expandIndicatorImageView;

        public MainStreamViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_main_stream_genre_name);
            expandIndicatorImageView = itemView.findViewById(R.id.image_view_expand_indicator);
        }


        @Override
        public void bind(@NonNull Integer coordinate) {
            itemView.setOnClickListener((view) -> {
                toggle(coordinate);
                if (expandedParents.remove(coordinate)) {
                    setState(false);
                } else {
                    setState(true);
                    expandedParents.add(coordinate);
                }
            });
            nameTextView.setText(mainStreamGenres.get(coordinate).name);
            setState(expandedParents.contains(coordinate));
        }

        private void setState(boolean expand) {
            if (!expand) {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_down);
            } else {
                expandIndicatorImageView.setImageResource(R.drawable.ic_arrow_up);
            }
        }

    }

    private class SubGenreViewHolder extends ChildViewHolder {


        private TextView nameTextView;

        public SubGenreViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_sub_genre_name);
        }

        @Override
        public void bind(@NonNull ChildCoordinate coordinate) {
            nameTextView.setText(
                    subGenres.get(mainStreamGenres.get(coordinate.parentIndex))
                            .get(coordinate.childRelativeIndex)
                            .name);
        }

    }
}
