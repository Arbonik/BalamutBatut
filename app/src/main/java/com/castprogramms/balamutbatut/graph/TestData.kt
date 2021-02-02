package com.example.graphguilibrary

object TestData {
    val list = mutableListOf<Test>(
        Test(
            "Ачивка 1",
            "Оно живое!"
        ),
        Test(
            "Ачивка 2",
            "Оно до сих пор живое"
        ),
        Test(
                "Ачивка 3",
                "Оно до сих пор живое"
        ),
        Test(
                "Ачивка 4",
                "Оно до сих пор живое"
        )
    )
}

class Test(
    var name : String,
    var description: String
){
    override fun toString(): String {
        return name + "\n" + description
    }
}