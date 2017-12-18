package com.umut.petexpandable;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView view = (RecyclerView) findViewById(R.id.recycler_view_music_genres);
        MusicExpandableViewAdapter adapter = new MusicExpandableViewAdapter(
                TestData.mainStreamGenres,
                TestData.subGenres);
        view.setAdapter(adapter);

        for (int i = 0; i < TestData.mainStreamGenres.size(); i++) {
            adapter.toggle(i);
        }
    }
}
