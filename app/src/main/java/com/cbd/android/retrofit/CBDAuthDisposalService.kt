package com.cbd.android.retrofit

import com.cbd.android.models.RequestNewPost
import com.cbd.android.models.ResponseGeneric
import com.cbd.android.models.ResponseListPost
import com.cbd.android.models.ResponseUser
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface CBDAuthDisposalService {

    @GET("post/list")
    fun getAllPosts(): Call<ResponseListPost>

    @POST("post/create")
    fun createPost(@Body requestNewPost: RequestNewPost): Call<ResponseGeneric>

    @GET("user/me")
    fun getUserLogged(): Call<ResponseUser>
}