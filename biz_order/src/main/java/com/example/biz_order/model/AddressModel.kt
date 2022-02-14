package com.example.biz_order.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class AddressModel(val total: Int, val list: List<Address>)

@Parcelize
data class Address(
    var province: String,
    var city: String,
    var area: String,
    var detail: String,
    var `receiver`: String,
    var phoneNum: String,
    val id: String,
    val uid: String
) : Parcelable