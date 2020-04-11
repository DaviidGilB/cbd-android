package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class Info(

    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String


)