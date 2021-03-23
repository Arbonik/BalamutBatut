package com.castprogramms.balamutbatut

import android.app.Application
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BalamutApplication : Application() {

    private val appModule = module {
        single<DataUserFirebase> {DataUserFirebase()}
        single<Repository> {Repository(get())}
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BalamutApplication)
            modules(appModule)
        }
    }

}