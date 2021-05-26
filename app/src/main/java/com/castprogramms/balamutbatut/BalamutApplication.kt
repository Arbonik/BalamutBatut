package com.castprogramms.balamutbatut

import android.app.Application
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.ui.changeprogram.ChangeElementsViewModel
import com.castprogramms.balamutbatut.ui.group.GroupViewModel
import com.castprogramms.balamutbatut.ui.allElements.AllElementListViewModel
import com.castprogramms.balamutbatut.ui.group.StudentsViewModel
import com.castprogramms.balamutbatut.ui.infostudent.InfoStudentViewModel
import com.castprogramms.balamutbatut.ui.qrcode.QrCodeViewModel
import com.castprogramms.balamutbatut.ui.rating.RatingViewModel
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BalamutApplication : Application() {

    private val appModule = module {
        single{DataUserFirebase(this@BalamutApplication.applicationContext)}
        single{Repository(get())}
        viewModel { ChangeElementsViewModel(get()) }
        viewModel {InfoStudentViewModel(get())}
        viewModel {StudentsViewModel(get())}
        viewModel {RegistrViewModel(get())}
        viewModel {RatingViewModel(get())}
        viewModel {GroupViewModel(get())}
        viewModel { QrCodeViewModel(get())}
        viewModel {AllElementListViewModel(get())}
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