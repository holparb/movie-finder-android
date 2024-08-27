package com.holparb.moviefinder.movies.data.entity.type_converter

import androidx.room.TypeConverter

class IntListTypeConverter {
    @TypeConverter
    fun fromIntListToString(list: List<Int>): String = list.toString()

    @TypeConverter
    fun fromStringToIntList(string: String): List<Int> {
        return string.removeSurrounding("[", "]").split(",").map { it.toInt() }
    }
}