package com.castprogramms.balamutbatut.users

import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.UserContacts

class Student(
    first_name: String,
    second_name: String,
    date: String,
    sex: String,
    img: String,
    var elements: List<Element>,
    var element: Map<String, List<Int>> = mapOf(),
    var batutcoin: Int = 0,
    var referId: String = "",
    var userContacts: UserContacts = UserContacts()
):Person(first_name, second_name, date, sex, groupID = notGroup, type = "student", img = img){
    fun getQuantityElements(): String {
        var score = 0
        element.forEach {
            score += it.value.size
        }
        return "Элементы: $score"
    }
    constructor() :this("","","","","", listOf())
}