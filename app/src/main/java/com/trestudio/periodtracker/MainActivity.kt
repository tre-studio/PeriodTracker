package com.trestudio.periodtracker

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.trestudio.periodtracker.components.main.MainLayout
import com.trestudio.periodtracker.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // dev mode
        val dbFile = applicationContext.getDatabasePath("app_database")
        if (dbFile.exists()) {
            dbFile.delete()
        }

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        )[MainViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MainLayout(viewModel)
        }
    }
}