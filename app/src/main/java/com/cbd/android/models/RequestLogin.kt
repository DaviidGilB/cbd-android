package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class RequestLogin(

    @SerializedName("username")
    val username: String,

    @SerializedName("password")
    val password: String
)