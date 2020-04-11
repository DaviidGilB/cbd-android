package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class RequestRegister(

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)