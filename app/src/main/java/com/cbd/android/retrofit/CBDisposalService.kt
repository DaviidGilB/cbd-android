package com.cbd.android.retrofit

import com.cbd.android.models.RequestLogin
import com.cbd.android.models.RequestRegister
import com.cbd.android.models.ResponseAuth
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CBDisposalService {

    @POST("auth/login")
    fun login(@Body requestLogin: RequestLogin): Call<ResponseAuth>

    @POST("auth/register")
    fun register(@Body requestRegister: RequestRegister): Call<ResponseAuth>

}