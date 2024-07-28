package com.trestudio.periodtracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.trestudio.periodtracker.viewmodel.database.AppDatabase
import com.trestudio.periodtracker.viewmodel.database.GettingStartedData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(app: Application) : AndroidViewModel(app) {
    private val db = Room.databaseBuilder(
        app,
        AppDatabase::class.java,
        "app_database"
    ).build()

    suspend fun getIntroStatus() = db.gettingStartedDao().gettingStartedData()?.done
    suspend fun completeIntro(): Boolean {
        db.gettingStartedDao().insert(GettingStartedData(1, true))
        return true
    }

    override fun onCleared() {
        super.onCleared()
        db.close()
    }
}