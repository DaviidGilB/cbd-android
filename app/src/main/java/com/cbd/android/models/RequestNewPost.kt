package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class RequestNewPost(

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("photo")
    val photo: String
)