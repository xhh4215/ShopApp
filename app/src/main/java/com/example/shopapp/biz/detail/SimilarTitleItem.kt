package com.example.shopapp.biz.detail

import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder
import com.example.shopapp.R

class SimilarTitleItem : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}