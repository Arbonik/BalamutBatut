package com.castprogramms.balamutbatut

import android.app.Application
import com.castprogramms.balamutbatut.network.DataUserFirebase
import com.castprogramms.balamutbatut.network.ReferralDynamicLinks
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.VideoAndDescFirebaseStorage
import com.castprogramms.balamutbatut.ui.addelements.AddElementsViewModel
import com.castprogramms.balamutbatut.ui.addstudents.AddStudentViewModel
import com.castprogramms.balamutbatut.ui.allgroupelement.AllGroupElementViewModel
import com.castprogramms.balamutbatut.ui.changeprogram.ChangeElementsViewModel
import com.castprogramms.balamutbatut.ui.editelement.EditElementViewModel
import com.castprogramms.balamutbatut.ui.editprofile.EditProfileViewModel
import com.castprogramms.balamutbatut.ui.group.GroupViewModel
import com.castprogramms.balamutbatut.ui.group.StudentsViewModel
import com.castprogramms.balamutbatut.ui.groupelements.GroupElementsViewModel
import com.castprogramms.balamutbatut.ui.infoelementfragment.InfoElementViewModel
import com.castprogramms.balamutbatut.ui.profile.ProfileViewModel
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
        single { DataUserFirebase(this@BalamutApplication.applicationContext) }
        single { VideoAndDescFirebaseStorage() }
        single { ReferralDynamicLinks() }
        single { Repository(get(), get(), get()) }
        viewModel {ChangeElementsViewModel(get())}
        viewModel {StudentsViewModel(get())}
        viewModel {RegistrViewModel(get())}
        viewModel {RatingViewModel(get())}
        viewModel {GroupViewModel(get())}
        viewModel {QrCodeViewModel(get())}
        viewModel {ProfileViewModel(get())}
        viewModel {EditElementViewModel(get())}
        viewModel {AddElementsViewModel(get())}
        viewModel {GroupElementsViewModel(get())}
        viewModel {AddStudentViewModel(get())}
        viewModel {AllGroupElementViewModel(get())}
        viewModel {InfoElementViewModel(get())}
        viewModel {EditProfileViewModel(get())}
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