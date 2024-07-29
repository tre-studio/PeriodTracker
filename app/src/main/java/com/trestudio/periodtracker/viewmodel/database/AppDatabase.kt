package com.trestudio.periodtracker.viewmodel.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [GettingStartedData::class, LMPstartDate::class, NoteDB::class], version = 1)
@TypeConverters(LMPConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gettingStartedDao(): GettingStartedDAO
    abstract fun LMPstartDateDao(): LMPstartDateDao
    abstract fun noteDbDao(): NoteDbDAO
}
