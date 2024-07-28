package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*

@Entity(tableName = "getting_started")
data class GettingStartedData(
    @PrimaryKey val id: Int = 1,
    val done: Boolean = false,
)

@Dao
interface GettingStartedDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: GettingStartedData)

    @Query("select * from getting_started WHERE id=1")
    suspend fun gettingStartedData(): GettingStartedData?
}