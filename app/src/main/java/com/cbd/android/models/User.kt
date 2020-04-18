package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("username")
    val username: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("email")
    val email: String
)