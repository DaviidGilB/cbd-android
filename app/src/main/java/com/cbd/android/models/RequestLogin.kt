package com.cbd.android.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestLogin(

    @Expose
    @SerializedName("username")
    val username: String,

    @Expose
    @SerializedName("password")
    val password: String
)