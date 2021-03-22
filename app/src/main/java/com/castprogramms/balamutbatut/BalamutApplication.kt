package com.castprogramms.balamutbatut

import android.app.Application

class BalamutApplication : Application() {
    companion object {
        val repository : Repository by lazy { Repository()}
    }
}