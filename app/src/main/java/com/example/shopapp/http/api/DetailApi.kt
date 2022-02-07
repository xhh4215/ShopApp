package com.example.shopapp.http.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.shopapp.model.DetailModel


interface DetailApi {
    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId: String): HiCall<DetailModel>
}