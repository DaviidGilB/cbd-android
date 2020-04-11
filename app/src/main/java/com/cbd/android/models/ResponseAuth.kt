package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class ResponseAuth(

    @SerializedName("info")
    val info: Info,

    @SerializedName("object")
    val `object`: String
)