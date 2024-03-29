package com.example.datatrap.databaseio.converters

import androidx.room.TypeConverter
import java.util.*

class DateLongConverters {
    @TypeConverter
    fun fromDateToLong(date: Date?): Long?{
        return date?.time
    }

    @TypeConverter
    fun fromLongToDate(date: Long?): Date?{
        return if (date == null) null else Date(date)
    }
}