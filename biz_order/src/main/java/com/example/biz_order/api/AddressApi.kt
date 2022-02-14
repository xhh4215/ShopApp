package com.example.biz_order.api

import com.example.biz_order.model.AddressModel
import com.example.library.restful.HiCall
import com.example.library.restful.annotation.*


interface AddressApi {
    @GET("address")
    fun queryAddress(
        @Field("pageIndex") pageIndex: Int,
        @Field("pageSize") pageSize: Int
    ): HiCall<AddressModel>


    /**
     * "area": "西湖区",
    "city": "杭州",
    "detail": "小西湖123号",
    "phoneNum": 15888888888,
    "province": "浙江",
    "receiver": "张三"
     */
    @POST("address", fromPost = false)
    fun addAddress(
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("area") area: String,
        @Field("detail") detail: String,
        @Field("receiver") receiver: String,
        @Field("phoneNum") phoneNum: String
    ): HiCall<String>

    @PUT("address/{id}", formPost = false)
    fun updateAddress(
        @Path("id") id: String,
        @Field("province") province: String,
        @Field("city") city: String,
        @Field("area") area: String,
        @Field("detail") detail: String,
        @Field("receiver") receiver: String,
        @Field("phoneNum") phoneNum: String
    ): HiCall<String>

    @DELETE("address/{id}")
    fun deleteAddress(@Path("id") id: String): HiCall<String>

}