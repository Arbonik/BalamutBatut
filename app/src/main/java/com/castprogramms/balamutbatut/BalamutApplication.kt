package com.castprogramms.balamutbatut

import android.app.Application
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.group.StudentsViewModel
import com.castprogramms.balamutbatut.ui.infostudent.InfoStudentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BalamutApplication : Application() {

    private val appModule = module {
        single<DataUserFirebase> {DataUserFirebase()}
        single<Repository> {Repository(get())}

        viewModel {InfoStudentViewModel(get())}
        viewModel {StudentsViewModel(get())}
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