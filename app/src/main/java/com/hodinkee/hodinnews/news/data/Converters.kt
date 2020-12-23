package com.hodinkee.hodinnews.news.data

import androidx.room.TypeConverter
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromCategory(value: Category): Int {
        return value.ordinal
    }

    @TypeConverter
    fun toCategory(value: Int): Category {
        return Category.values()[value]
    }
}