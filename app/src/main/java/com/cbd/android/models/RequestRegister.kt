package com.cbd.android.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestRegister(

    @Expose
    @SerializedName("username")
    val username: String,

    @Expose
    @SerializedName("email")
    val email: String,

    @Expose
    @SerializedName("password")
    val password: String
)