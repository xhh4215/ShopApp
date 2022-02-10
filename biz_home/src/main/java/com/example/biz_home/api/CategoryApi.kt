package com.example.biz_home.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.biz_home.model.Subcategory
import com.example.biz_home.model.TabCategory

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}