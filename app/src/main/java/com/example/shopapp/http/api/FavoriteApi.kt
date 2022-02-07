package com.example.shopapp.http.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.POST
import com.example.library.restful.annotation.Path
import com.example.shopapp.model.Favorite

interface FavoriteApi{

    @POST("favorites/{goodsId}")
    fun favorite(@Path("goodsId")goodsId:String): HiCall<Favorite>
}