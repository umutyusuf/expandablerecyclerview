package com.umut.petexpandable;

import androidx.annotation.NonNull;

import com.umut.expandablrecyclerview.adapter.data.ExpandableDataIndexProvider;
import com.umut.petexpandable.model.MainStreamGenre;
import com.umut.petexpandable.model.SubGenre;

import java.util.List;
import java.util.Map;

public class SampleDataIndexProvider implements ExpandableDataIndexProvider {

    private final List<MainStreamGenre> mainStreamGenres;
    private final Map<MainStreamGenre, List<SubGenre>> subGenres;

    public SampleDataIndexProvider(@NonNull List<MainStreamGenre> mainStreamGenres,
                                   @NonNull Map<MainStreamGenre, List<SubGenre>> subGenres) {
        this.mainStreamGenres = mainStreamGenres;
        this.subGenres = subGenres;
    }

    @Override
    public int getChildrenSize(int parentIndex) {
        return subGenres.get(mainStreamGenres.get(parentIndex)).size();
    }

    @Override
    public int getParentSize() {
        return mainStreamGenres.size();
    }

}
