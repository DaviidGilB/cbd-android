package com.cbd.android.models

import com.google.gson.annotations.SerializedName

data class ResponseListPost(

    @SerializedName("info")
    val info: Info,

    @SerializedName("object")
    val posts: List<Post>
)