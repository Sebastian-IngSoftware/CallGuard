package com.callguard.app.data.local

import androidx.room.TypeConverter
import com.callguard.app.domain.model.MatchType

class Converters {
    @TypeConverter
    fun fromMatchType(value: MatchType): String = value.name

    @TypeConverter
    fun toMatchType(value: String): MatchType = MatchType.valueOf(value)
}
