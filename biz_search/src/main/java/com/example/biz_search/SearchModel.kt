package com.example.biz_search

import com.example.pub_mod.model.GoodsModel
import java.io.Serializable

data class QuickSearchList(
    val list: List<KeyWord>,
    val total: Int
)

data class KeyWord(
    val id: String?,
    val keyWord: String
) : Serializable

data class GoodsSearchList(val total: Int, val list: List<GoodsModel>)