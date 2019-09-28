package com.umut.petexpandable

import com.umut.petexpandable.model.MainStreamGenre
import com.umut.petexpandable.model.SubGenre
import java.util.*

object TestData {

    var mainStreamGenres: MutableList<MainStreamGenre>
    var subGenres: Map<MainStreamGenre, List<SubGenre>>

    private val jazzGenres: List<SubGenre>
        get() {
            val jazzSubGenres = ArrayList<SubGenre>()
            jazzSubGenres.add(SubGenre("Cape Jazz"))
            jazzSubGenres.add(SubGenre("Cool Jazz"))
            jazzSubGenres.add(SubGenre("Smooth Jazz"))
            jazzSubGenres.add(SubGenre("Swing"))
            jazzSubGenres.add(SubGenre("Neo-swing"))
            return jazzSubGenres
        }

    private val bluesGenres: List<SubGenre>
        get() {
            val bluesSubGenres = ArrayList<SubGenre>()
            bluesSubGenres.add(SubGenre("Blues Rock"))
            bluesSubGenres.add(SubGenre("Boogie-woogie"))
            bluesSubGenres.add(SubGenre("British Blues"))
            bluesSubGenres.add(SubGenre("Black Blues"))
            bluesSubGenres.add(SubGenre("Soul Blues"))
            return bluesSubGenres
        }

    private val rockGenres: List<SubGenre>
        get() {
            val rockSubGenres = ArrayList<SubGenre>()
            rockSubGenres.add(SubGenre("Acid Rock"))
            rockSubGenres.add(SubGenre("Alternative Rock"))
            rockSubGenres.add(SubGenre("Grunge"))
            rockSubGenres.add(SubGenre("PostGrunge"))
            return rockSubGenres
        }

    init {
        mainStreamGenres = createMainStreamGenres()
        for (i in 0..9) {
            mainStreamGenres.addAll(createMainStreamGenres())
        }

        subGenres = getSubGenres(mainStreamGenres)

    }


    private fun getSubGenres(mainStreamGenres: List<MainStreamGenre>): Map<MainStreamGenre, List<SubGenre>> {
        val subGenres = HashMap<MainStreamGenre, List<SubGenre>>()
        for (genre in mainStreamGenres) {
            if (genre.name == "ROCK") {
                subGenres[genre] = rockGenres
                continue
            }
            if (genre.name == "BLUES") {
                subGenres[genre] = bluesGenres
                continue
            }
            if (genre.name == "JAZZ") {
                subGenres[genre] = jazzGenres
            }

        }
        return subGenres
    }

    private fun createMainStreamGenres(): MutableList<MainStreamGenre> {
        return ArrayList(
            listOf(
                MainStreamGenre("ROCK"),
                MainStreamGenre("BLUES"), MainStreamGenre("JAZZ")
            )
        )
    }
}
