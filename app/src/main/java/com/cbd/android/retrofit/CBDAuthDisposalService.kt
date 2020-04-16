package com.cbd.android.retrofit

import com.cbd.android.models.ResponseListPost
import retrofit2.Call
import retrofit2.http.GET

interface CBDAuthDisposalService {

    @GET("post/list")
    fun getAllPosts(): Call<ResponseListPost>

}