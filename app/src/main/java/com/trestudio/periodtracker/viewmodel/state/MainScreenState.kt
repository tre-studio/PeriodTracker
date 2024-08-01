package com.trestudio.periodtracker.viewmodel.state

enum class MainScreenState {
    // Icon home on navbar
    MainApp,
    Setting,
    Timeline,
    Note,

    // Icon qr code on navbar
    QRcode,

    // Help on navbar
    Help,
    ;

    fun belongsToMainScreen(): Boolean {
        return this == MainApp ||
                this == Setting ||
                this == Timeline ||
                this == Note
    }
}