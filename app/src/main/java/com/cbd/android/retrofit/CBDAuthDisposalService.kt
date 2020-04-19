package com.cbd.android.retrofit

import com.cbd.android.models.RequestNewPost
import com.cbd.android.models.ResponseGeneric
import com.cbd.android.models.ResponseListPost
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CBDAuthDisposalService {

    @GET("post/list")
    fun getAllPosts(): Call<ResponseListPost>

    @POST("post/create")
    fun createPost(@Body requestNewPost: RequestNewPost): Call<ResponseGeneric>
}