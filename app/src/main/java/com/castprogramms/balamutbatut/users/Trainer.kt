package com.castprogramms.balamutbatut.users

class Trainer(
    first_name: String,
    second_name: String,
    date: String,
    sex: String,
    var groupID : String
):Person(first_name, second_name, date, sex) {
    override fun toString(): String {
        return "Trainer(first_name: $first_name, second_name: $second_name, date: $date, sex: $sex, groupID: $groupID)"
    }
}