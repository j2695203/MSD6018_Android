package com.example.lab4

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date


//apparently Room can't handle Date objects directly...
class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }
}


//Defines a SQLITE table, basically
@Entity(tableName="joke")
data class JokeData(var timestamp: Date,
                       var joke: String){
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0 // integer primary key for the DB
}