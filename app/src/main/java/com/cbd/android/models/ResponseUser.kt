package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class ResponseUser(

    @SerializedName("info")
    val info: Info,

    @SerializedName("object")
    val user: User
)