package com.example.biz_home.api

import com.example.biz_home.model.Favorite
import com.example.library.restful.HiCall
import com.example.library.restful.annotation.POST
import com.example.library.restful.annotation.Path

interface FavoriteApi{

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId")goodsId:String): HiCall<Favorite>
}