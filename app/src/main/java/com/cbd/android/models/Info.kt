package com.cbd.android.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Info(

    @Expose
    @SerializedName("code")
    val code: Int,

    @Expose
    @SerializedName("message")
    val message: String


)