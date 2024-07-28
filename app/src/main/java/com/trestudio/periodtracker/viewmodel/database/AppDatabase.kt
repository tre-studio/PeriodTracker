package com.trestudio.periodtracker.viewmodel.database

import androidx.room.*

@Database(entities = [GettingStartedData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gettingStartedDao(): GettingStartedDAO
}
