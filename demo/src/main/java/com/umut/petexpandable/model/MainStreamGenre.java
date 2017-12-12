package com.umut.petexpandable.model;

import android.support.annotation.NonNull;

public class MainStreamGenre {

    @NonNull
    public final String name;

    private long fieldForTrick;

    public MainStreamGenre(@NonNull String name) {
        this.name = name;
        fieldForTrick = System.nanoTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MainStreamGenre genre = (MainStreamGenre) o;

        return fieldForTrick == genre.fieldForTrick;
    }

    @Override
    public int hashCode() {
        return (int) (fieldForTrick ^ (fieldForTrick >>> 32));
    }
}
