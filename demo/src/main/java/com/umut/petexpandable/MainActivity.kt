package com.umut.petexpandable


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<RecyclerView>(R.id.recycler_view_music_genres)
        val adapter = MusicExpandableViewAdapter(
            TestData.mainStreamGenres,
            TestData.subGenres
        )
        view.adapter = adapter

        for (i in TestData.mainStreamGenres.indices) {
            adapter.toggle(i)
        }
    }
}
