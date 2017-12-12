package com.umut.petexpandable;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umut.expandablrecyclerview.adapter.ExpandableViewAdapter;
import com.umut.expandablrecyclerview.adapter.ExpandableViewHolder;
import com.umut.petexpandable.model.MainStreamGenre;
import com.umut.petexpandable.model.SubGenre;

public class MusicExpandableViewAdapter extends ExpandableViewAdapter<MainStreamGenre, SubGenre> {

    public MusicExpandableViewAdapter() {
        super(new SampleDataProvider());
    }

    @Override
    protected ParentViewHolder createParentViewHolder(@NonNull ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_main_stream,
                container, false);
        return new ParentViewHolder(view);
    }

    @Override
    protected ChildViewHolder createChildViewHolder(@NonNull ViewGroup container) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_sub_genre,
                container, false);
        return new ChildViewHolder(view);
    }

    private class ParentViewHolder extends ExpandableViewHolder<MainStreamGenre> {

        private TextView nameTextView;

        public ParentViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_main_stream_genre_name);

        }

        @Override
        public void bind(MainStreamGenre value, int position) {
            if (value != null)
                nameTextView.setText(value.name);
            itemView.setOnClickListener(view -> toggle(position));
        }
    }

    private class ChildViewHolder extends ExpandableViewHolder<SubGenre> {


        private TextView nameTextView;

        public ChildViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_sub_genre_name);
        }

        @Override
        public void bind(SubGenre value, int position) {
            if (value != null)
                nameTextView.setText(value.name);
        }
    }
}
