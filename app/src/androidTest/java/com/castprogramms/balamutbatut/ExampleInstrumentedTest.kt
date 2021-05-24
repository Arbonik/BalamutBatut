    package com.castprogramms.balamutbatut

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.castprogramms.balamutbatut.tools.DataUserFirebase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

    /**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    val dataUser = DataUserFirebase(this@BalamutApplication.applicationContext)
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.castprogramms.balamutbatut", appContext.packageName)
    }

//    @Test
//    fun getGroups(){
//        val firebaseMock = Mockito.mock(DataUserFirebase::class.java)
//        assertEquals(firebaseMock.getAllGroup().value, mutableListOf<Group>())
//    }
}