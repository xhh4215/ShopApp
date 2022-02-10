package com.example.biz_home.api

import com.example.biz_home.model.CourseNotice
import com.example.library.restful.HiCall
import com.example.library.restful.annotation.GET


interface AccountApi {

    @GET("notice")
    fun notice(): HiCall<CourseNotice>
}