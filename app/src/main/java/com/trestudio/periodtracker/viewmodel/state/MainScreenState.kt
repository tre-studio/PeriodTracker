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

    private fun isMainComponent(): Boolean = this == MainApp || this == QRcode || this == Help

    fun belongsToMainScreen(): Boolean = this == MainApp || this == Setting || this == Timeline || this == Note
    fun isInTheLeft(next: MainScreenState): Boolean? =
        if (this.isMainComponent() && next.isMainComponent()) this < next else null
}