package com.example.shopapp.fragment.home

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.ui.component.HiAbsListFragment
import com.example.hi.item.HiDataItem
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.library.restful.annotation.CacheStrategy
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.HomeApi
import com.example.shopapp.model.HomeModel

open class HomeTabFragment : HiAbsListFragment() {
    private val homeModel: HomeViewModel by viewModels()
    private val DEFAULT_HOT_TAB_CATEGORY_ID = "1"
    private var categoryId: String? = null

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment = HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_HOT_TAB_CATEGORY_ID)
        super.onViewCreated(view, savedInstanceState)
        enableLoadMore {
            queryCategoryList(CacheStrategy.NET_ONLY)
        }
        queryCategoryList(CacheStrategy.CACHE_FIRST)
    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun queryCategoryList(cacheStrategy: Int) {
        homeModel.queryTabCategoryList(categoryId, pageIndex, cacheStrategy)
            .observe(viewLifecycleOwner,
                Observer {
                    if (it != null) {
                        updateUI(it)
                    } else {
                        finishRefresh(null)
                    }
                })
    }

    override fun onRefresh() {
        super.onRefresh()
        queryCategoryList(CacheStrategy.NET_CACHE)
    }


    private fun updateUI(data: HomeModel) {
         val dataItems = mutableListOf<HiDataItem<*, *>>()
        data.bannerList?.let {
            dataItems.add(BannerItem(data.bannerList))
        }
        data.subcategoryList?.let {
            dataItems.add(GridItem(data.subcategoryList))
        }
        data.goodsList?.forEachIndexed { index, goodsModel ->
            dataItems.add(
                GoodItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dataItems)
    }
}