package com.castprogramms.balamutbatut.users

import java.net.URL

open class Person(
    var first_name: String,
    var second_name : String,
    var date : String,
    var sex : String,
) {
    companion object{
        val notGroup = "НетГруппы"
    }
}