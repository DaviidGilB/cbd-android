package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class ResponseGeneric(

    @SerializedName("info")
    val info: Info,

    @SerializedName("object")
    val token: String
)