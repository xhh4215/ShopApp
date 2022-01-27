package com.example.shopapp.http.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.shopapp.model.GoodsList


interface GoodsApi {
    @GET("goods/goods/{categoryId}")
    fun queryCategoryGoodsList(
        @Path("categoryId") categoryId: String,
        @Field("subcategoryId") subcategoryId: String,
        @Field("pageSize") pageSize: Int,
        @Field("pageIndex") pageIndex: Int
    ): HiCall<GoodsList>
}