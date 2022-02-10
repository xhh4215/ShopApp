package com.example.biz_home.goodlist

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.example.biz_home.api.GoodsApi
import com.example.common.ui.component.HiAbsListFragment
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
 import com.example.common.http.ApiFactory
 import com.example.biz_home.model.GoodsList
import com.example.common.route.HiRoute
import com.example.pub_mod.model.items.GoodItem

class GoodListFragment : HiAbsListFragment() {

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    companion object {
        fun newInstance(categoryId: String, subcategoryId: String): GoodListFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            args.putString("subcategoryId", subcategoryId)
            val fragment = GoodListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiRoute.inject(this)
        enableLoadMore {
            loadData()
        }
        loadData()
    }


    override fun onRefresh() {
        super.onRefresh()
        loadData()
    }

    private fun loadData() {
        ApiFactory.create(GoodsApi::class.java)
            .queryCategoryGoodsList(categoryId = categoryId, subcategoryId, 10, pageIndex).enqueue(
                object : HiCallBack<GoodsList> {
                    override fun onSuccess(response: HiResponse<GoodsList>) {
                        if (response.successful() && response.data != null) {
                            onQueryCategoryGoodList(response.data!!)
                        } else {
                            finishRefresh(null)
                        }
                    }

                    override fun onFailed(throwable: Throwable) {
                        finishRefresh(null)

                    }
                })

    }

    private fun onQueryCategoryGoodList(data: GoodsList) {
        val dataItems = mutableListOf<GoodItem>()
        for (goodModel in data.list) {
            val goodItem = GoodItem(goodModel, false)
            dataItems.add(goodItem)
        }
        finishRefresh(dataItems)

    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 2)
    }
}