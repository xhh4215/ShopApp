package com.example.shopapp.http.api

import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET
import com.example.library.restful.annotation.Path
import com.example.shopapp.model.Subcategory
import com.example.shopapp.model.TabCategory

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}