package com.tpa.questapp

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var fullname: String? = "",
    var major: String? = "",
    var location: String? = "",
    var gender: String? = "",
    var job: String? = ""
)