package com.example.biz_home.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.CacheStrategy
import com.example.library.restful.annotation.Field
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.biz_home.model.HomeModel
import com.example.biz_home.model.TabCategory

interface HomeApi {
    @CacheStrategy(CacheStrategy.NET_ONLY)
    @GET("category/categories")
    fun queryTabList(): HiCall<List<TabCategory>>

    @GET("home/{categoryId}")
    fun queryTabCategoryList(
        @CacheStrategy cacheStrategy: Int,
        @Path("categoryId") categoryId: String,
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): HiCall<HomeModel>
}