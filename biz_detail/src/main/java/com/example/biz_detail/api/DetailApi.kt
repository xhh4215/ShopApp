package com.example.biz_detail.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.biz_detail.model.DetailModel


interface DetailApi {
    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId: String): HiCall<DetailModel>
}