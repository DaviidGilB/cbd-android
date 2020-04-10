package com.cbd.android.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseAuth(

    @Expose
    @SerializedName("info")
    val info: Info,

    @Expose
    @SerializedName("object")
    val `object`: String
)