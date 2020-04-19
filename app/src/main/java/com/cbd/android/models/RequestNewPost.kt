package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class RequestNewPost(

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("title")
    val title: String
)