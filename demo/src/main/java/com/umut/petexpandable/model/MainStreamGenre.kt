package com.umut.petexpandable.model

class MainStreamGenre(val name: String) {

    private val fieldForTrick: Long = System.nanoTime()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false

        val genre = other as MainStreamGenre?

        return fieldForTrick == genre!!.fieldForTrick
    }

    override fun hashCode(): Int {
        return (fieldForTrick xor fieldForTrick.ushr(32)).toInt()
    }
}
