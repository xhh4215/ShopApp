package com.example.biz_login.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.POST
import com.example.service_login.UserProfile


interface AccountApi {
    @POST("user/login")
    fun login(
        @Field("userName") userName: String,
        @Field("password") password: String
    ): HiCall<String>


    @POST("user/registration")
    fun register(
        @Field("userName") userName: String,
        @Field("password") password: String,
        @Field("imoocId") imoocId:
        String, @Field("orderId") orderId: String
    ): HiCall<String>


    @GET("user/profile")
    fun profile(): HiCall<UserProfile>
}