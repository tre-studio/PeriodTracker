package com.trestudio.periodtracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.trestudio.periodtracker.viewmodel.database.*
import java.time.LocalDate

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    suspend fun getIntroStatus() = db.gettingStartedDao().gettingStartedData()?.done
    suspend fun completeIntro(): Boolean {
        db.gettingStartedDao().insert(GettingStartedData(1, true))
        return true
    }

    suspend fun completeLMPstartDate(date: LocalDate, avgCycle: UInt, avgPeriod: UInt) {
        db.LMPstartDateDao().insert(LMPstartDate.new(date, avgCycle, avgPeriod))
    }

    suspend fun getLMPstartDate() = db.LMPstartDateDao().getFirst()

    suspend fun addNotes(date: LocalDate, symptom: SymptonBuilder, painLevel: Int, otherNote: String) {
        db.noteDbDao().insert(NoteDB(null, date, symptom.build(), painLevel, otherNote))
    }

    suspend fun getNotesForMonth(value: LocalDate): List<NoteDB> {
        val pair = NoteDB.localDateToMonthAndString(value)
        return db.noteDbDao().getNotesForMonth(pair.first, pair.second)
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}