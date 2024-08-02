package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*
import java.time.LocalDate

@Entity(tableName = "lmp_start_date")
@TypeConverters(LMPConverters::class)
data class LMPstartDate(
    @PrimaryKey val id: Int = 1,
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