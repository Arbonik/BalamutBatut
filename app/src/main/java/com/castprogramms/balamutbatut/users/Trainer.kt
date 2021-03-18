package com.castprogramms.balamutbatut.users

import java.net.URL

class Trainer(
    first_name: String,
    second_name: String,
    date: String,
    sex: String,
    img: String,
    var groupID : String
):Person(first_name, second_name, date, sex, img) {
    override fun toString(): String {
        return "Trainer(first_name: $first_name, second_name: $second_name, date: $date, sex: $sex, groupID: $groupID)"
    }
}