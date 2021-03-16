package com.castprogramms.balamutbatut.users

open class Person(
    var first_name: String,
    var second_name : String,
    var date : String,
    var sex : String,
    var groupID: String,
    var type: String
) {
    companion object{
        val notGroup = "НетГруппы"
    }
}