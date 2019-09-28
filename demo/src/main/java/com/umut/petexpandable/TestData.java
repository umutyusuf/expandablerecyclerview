package com.umut.petexpandable;

import com.umut.petexpandable.model.MainStreamGenre;
import com.umut.petexpandable.model.SubGenre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TestData {

    private TestData() {

    }

    public static List<MainStreamGenre> mainStreamGenres;
    public static Map<MainStreamGenre, List<SubGenre>> subGenres;

    static {
        mainStreamGenres = getMainStreamGenres();
        for (int i = 0; i < 10; i++) {
            mainStreamGenres.addAll(getMainStreamGenres());
        }

        subGenres = getSubGenres(mainStreamGenres);

    }


    static Map<MainStreamGenre, List<SubGenre>> getSubGenres(List<MainStreamGenre> mainStreamGenres) {
        Map<MainStreamGenre, List<SubGenre>> subGenres = new HashMap<>();
        for (MainStreamGenre genre : mainStreamGenres) {
            if (genre.getName().equals("ROCK")) {
                subGenres.put(genre, getRockGenres());
                continue;
            }
            if (genre.getName().equals("BLUES")) {
                subGenres.put(genre, getBluesGenres());
                continue;
            }
            if (genre.getName().equals("JAZZ")) {
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
}
