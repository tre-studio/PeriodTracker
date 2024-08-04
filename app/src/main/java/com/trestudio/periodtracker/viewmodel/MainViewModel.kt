package com.trestudio.periodtracker.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.room.Room
import com.trestudio.periodtracker.algorithm.calendarDayRange
import com.trestudio.periodtracker.viewmodel.database.AppDatabase
import com.trestudio.periodtracker.viewmodel.database.GettingStartedData
import com.trestudio.periodtracker.viewmodel.database.LMPstartDate
import com.trestudio.periodtracker.viewmodel.database.NoteDB
import com.trestudio.periodtracker.viewmodel.state.MainScreenState
import com.trestudio.periodtracker.viewmodel.state.SettingButtonState
import com.trestudio.periodtracker.viewmodel.state.TimelineButtonState
import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app_database"
    )
        .fallbackToDestructiveMigration()
        .build()

    private val _currentMonth = mutableStateOf(LocalDate.now())
    val currentMonth: State<LocalDate> = _currentMonth


    fun setCurrentMonth(month: LocalDate) {
        _currentMonth.value = month
    }

    private val _mainScreenState = mutableStateOf(MainScreenState.MainApp)
    private val _settingButtonState = mutableStateOf(SettingButtonState.SettingButton)
    private val _timelineButtonState = mutableStateOf(TimelineButtonState.TimelineButton)

    val mainScreenState: State<MainScreenState> = _mainScreenState
    val settingButtonState: State<SettingButtonState> = _settingButtonState
    val timelineButtonState: State<TimelineButtonState> = _timelineButtonState

    fun setMainScreenState(state: MainScreenState) {
        _mainScreenState.value = state
    }

    fun switchSettingButtonState() {
        _settingButtonState.value = _settingButtonState.value.opposite()
    }

    fun switchTimelineButtonState() {
        _timelineButtonState.value = _timelineButtonState.value.oposite()
    }

    fun setSettingButtonState(value: SettingButtonState) {
        _settingButtonState.value = value
    }

    fun setTimelineButtonState(value: TimelineButtonState) {
        _timelineButtonState.value = value
    }

    suspend fun getIntroStatus() = db.gettingStartedDao().gettingStartedData()?.done
    suspend fun completeIntro(): Boolean {
        db.gettingStartedDao().insert(GettingStartedData(1, true))
        return true
    }

    suspend fun completeLMPstartDate(date: LocalDate, avgCycle: UInt, avgPeriod: UInt) {
        db.LMPstartDateDao().insert(LMPstartDate.new(date, avgCycle, avgPeriod))
    }

    suspend fun getLMPstartDate() = db.LMPstartDateDao().getFirst()

    suspend fun addNote(notes: NoteDB) = db.noteDbDao().insert(notes)
    suspend fun getNotesForMonth(value: LocalDate): List<NoteDB> {
        val (start, end) = calendarDayRange(value, WeekFields.of(Locale.getDefault()))
        return db.noteDbDao().getMonthlyNotes(start, end)
    }

    suspend fun getAllNotes(): List<NoteDB> {
        return db.noteDbDao().getAllNotes()
    }

    suspend fun updateNote(notes: NoteDB) = db.noteDbDao().update(notes)
    suspend fun deleteNote(notes: NoteDB) = db.noteDbDao().delete(notes)

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}