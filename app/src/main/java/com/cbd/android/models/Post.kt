package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class Post(

    @SerializedName("date")
    val date: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    @SerializedName("title")
    val title: String,

    @SerializedName("userId")
    val userId: String,

    @SerializedName("photo")
    val photo: String,

    @SerializedName("user")
    val user: User
)