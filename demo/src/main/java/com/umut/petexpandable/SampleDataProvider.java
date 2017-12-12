package com.umut.petexpandable;

import android.support.annotation.NonNull;

import com.umut.expandablrecyclerview.adapter.ChildCoordinate;
import com.umut.expandablrecyclerview.adapter.data.ExpandableDataProvider;
import com.umut.petexpandable.model.MainStreamGenre;
import com.umut.petexpandable.model.SubGenre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SampleDataProvider implements ExpandableDataProvider<MainStreamGenre, SubGenre> {
    public static List<MainStreamGenre> mainStreamGenres;
    private static Map<MainStreamGenre, List<SubGenre>> subGenres;

    static {
        mainStreamGenres = getMainStreamGenres();
        for (int i = 0; i < 1000; i++) {
            mainStreamGenres.addAll(getMainStreamGenres());
        }

        subGenres = getSubGenres(mainStreamGenres);

    }


    static Map<MainStreamGenre, List<SubGenre>> getSubGenres(List<MainStreamGenre> mainStreamGenres) {
        Map<MainStreamGenre, List<SubGenre>> subGenres = new HashMap<>();
        for (MainStreamGenre genre : mainStreamGenres) {
            if (genre.name.equals("ROCK")) {
                subGenres.put(genre, getRockGenres());
                continue;
            }
            if (genre.name.equals("BLUES")) {
                subGenres.put(genre, getBluesGenres());
                continue;
            }
            if (genre.name.equals("JAZZ")) {
                subGenres.put(genre, getJazzGenres());
            }

        }
        return subGenres;
    }

    private static List<SubGenre> getJazzGenres() {
        List<SubGenre> jazzSubGenres = new ArrayList<>();
        jazzSubGenres.add(new SubGenre("Cape Jazz"));
        jazzSubGenres.add(new SubGenre("Cool Jazz"));
        jazzSubGenres.add(new SubGenre("Smooth Jazz"));
        jazzSubGenres.add(new SubGenre("Swing"));
        jazzSubGenres.add(new SubGenre("Neo-swing"));
        return jazzSubGenres;
    }

    private static List<SubGenre> getBluesGenres() {
        List<SubGenre> bluesSubGenres = new ArrayList<>();
        bluesSubGenres.add(new SubGenre("Blues Rock"));
        bluesSubGenres.add(new SubGenre("Boogie-woogie"));
        bluesSubGenres.add(new SubGenre("British Blues"));
        bluesSubGenres.add(new SubGenre("Black Blues"));
        bluesSubGenres.add(new SubGenre("Soul Blues"));
        return bluesSubGenres;
    }

    private static List<SubGenre> getRockGenres() {
        List<SubGenre> rockSubGenres = new ArrayList<>();
        rockSubGenres.add(new SubGenre("Acid Rock"));
        rockSubGenres.add(new SubGenre("Alternative Rock"));
        rockSubGenres.add(new SubGenre("Grunge"));
        rockSubGenres.add(new SubGenre("PostGrunge"));
        return rockSubGenres;
    }

    static List<MainStreamGenre> getMainStreamGenres() {
        return new ArrayList<>(Arrays.asList(new MainStreamGenre("ROCK"),
                new MainStreamGenre("BLUES"), new MainStreamGenre("JAZZ")));
    }

    @NonNull
    @Override
    public List<SubGenre> getChildren(@NonNull MainStreamGenre parent) {
        return subGenres.get(parent);
    }

    @NonNull
    @Override
    public SubGenre getChild(@NonNull ChildCoordinate coordinate) {
        return subGenres.get(mainStreamGenres.get(coordinate.parentIndex)).get(coordinate.childRelativeIndex);
    }

    @Override
    public int getChildrenSize(MainStreamGenre parent) {
        return subGenres.get(parent).size();
    }

    @Override
    public int getParentSize() {
        return mainStreamGenres.size();
    }

    @Override
    public MainStreamGenre getParent(int index) {
        return mainStreamGenres.get(index);
    }
}
