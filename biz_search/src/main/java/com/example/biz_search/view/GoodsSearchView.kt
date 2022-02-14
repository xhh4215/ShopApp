package com.example.biz_search.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hi.input.HiRecyclerView
import com.example.hi.item.HiAdapter
import com.example.pub_mod.model.GoodsModel
import com.example.pub_mod.model.items.GoodItem

class GoodsSearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : HiRecyclerView(context, attrs, defStyleAttr) {

    init {
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = HiAdapter(context)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    }

    fun bindData(
        list: List<GoodsModel>,
        loadInit: Boolean
    ) {
        val dataItems = mutableListOf<GoodItem>()
        for (goodsModel in list) {
            dataItems.add(GoodItem(goodsModel, true))
        }
        val hiAdapter = adapter as HiAdapter
        if (loadInit) hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
    }
}