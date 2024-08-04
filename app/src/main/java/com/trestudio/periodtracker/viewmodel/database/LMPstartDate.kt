package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Entity(tableName = "lmp_start_date")
@TypeConverters(LMPConverters::class)
@Serializable
data class LMPstartDate(
    @PrimaryKey val id: Int = 1,
    @Serializable(with = LocalDateSerializer::class)
    val value: LocalDate,
    val avgCycle: Int,
    val avgPeriod: Int,
) {
    companion object {
        fun new(lmpStartDate: LocalDate, avgCycle: UInt, avgPeriod: UInt): LMPstartDate {
            return LMPstartDate(1, lmpStartDate, avgCycle.toInt(), avgPeriod.toInt())
        }
    }
}

// Reference: https://stackoverflow.com/questions/77002137/serializer-has-not-been-found-for-type-localdate
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalDate::class)
class LocalDateSerializer : KSerializer<LocalDate> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        return LocalDate.parse(decoder.decodeString(), formatter)
    }
}

class LMPConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}

@Dao
interface LMPstartDateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: LMPstartDate)

    @Query("select * from lmp_start_date WHERE id=1")
    suspend fun getFirst(): LMPstartDate?
}