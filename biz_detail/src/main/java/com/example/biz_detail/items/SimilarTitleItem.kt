package com.example.biz_detail.items

import com.example.biz_detail.R
import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder

class SimilarTitleItem : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}