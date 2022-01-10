package com.example.shopapp.http

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.POST

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


}