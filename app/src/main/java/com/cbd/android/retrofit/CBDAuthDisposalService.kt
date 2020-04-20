package com.cbd.android.retrofit

import com.cbd.android.models.ResponseGeneric
import com.cbd.android.models.ResponseListPost
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface CBDAuthDisposalService {

    @GET("post/list")
    fun getAllPosts(): Call<ResponseListPost>

    @Multipart
    @POST("post/create")
    fun createPost(@Part("title") title: String, @Part("description") description: String, @Part("price") price: Double): Call<ResponseGeneric>
}