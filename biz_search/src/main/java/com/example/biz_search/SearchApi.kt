package com.example.biz_search

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.POST


interface SearchApi {
    @GET("goods/search/quick")
    fun quickSearch(@Field("keyWord") keyword: String): HiCall<QuickSearchList>

    @POST("goods/search", fromPost = false)
    fun goodsSearch(
        @Field("keyWord") keyword: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): HiCall<GoodsSearchList>
}