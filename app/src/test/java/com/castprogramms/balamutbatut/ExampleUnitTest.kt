package com.castprogramms.balamutbatut

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

sealed class WHAT() {
    class NOONW : WHAT()
    class YEEAH : WHAT()
}
class ExampleUnitTest {

    fun getMeForce():LiveData<WHAT>{
        val ret = MutableLiveData<WHAT>(WHAT.NOONW())
        GlobalScope.launch {
            delay((1000..2000).random().toLong())
            ret.postValue(WHAT.YEEAH())
        }
        return ret
    }

    @Test
    fun addition_isCorrect() {
        getMeForce().observeForever {
            println(it)
        }
        readLine()
    }
}