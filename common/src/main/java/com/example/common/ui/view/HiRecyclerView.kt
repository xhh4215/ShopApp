package com.example.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.common.R
import com.example.hi.banner.core.HIBanner
import com.example.hi.item.HiAdapter

open class HiRecyclerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {

    private var loadScrollerListener: OnScrollListener? = null
    private var footerView: View? = null
    private var isLoadingMore = false


    inner class LoadScrollerListener(val prefetchSize: Int, val callback: () -> Unit) :
        RecyclerView.OnScrollListener() {

        private val hiAdapter = adapter as HiAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            //根据滑动状态进行操作
            if (isLoadingMore) {
                return
            }
            //获取列表上显示的数据的数量
            val totalItemCount = hiAdapter.itemCount
            if (totalItemCount <= 0) return
            var canScrollerVertical = canScrollVertically(1)
            val lastVisibleItem = findLastVisibleItem(recyclerView)
            if (lastVisibleItem <= 0) return
            val arriveBottom = lastVisibleItem >= totalItemCount - 1
            //recyclerview 在拖动的状态下添加footerView
            //判断recyclerView是否能向下滑动  或者已经在拖动列表
            if (newState == SCROLL_STATE_DRAGGING && (canScrollerVertical || arriveBottom)) {
                addFooterView()
            }
            //不能在滑动停止的时候添加footer
            if (newState != SCROLL_STATE_IDLE) {
                return
            }
            //预加载
            val arrivePrefetchPosition = totalItemCount - lastVisibleItem <= prefetchSize
            if (!arrivePrefetchPosition) return
            isLoadingMore = true
            callback()

        }

        private fun addFooterView() {
            val footerView = getFooterView()
            //边界场景会出现多次添加
            if (footerView.parent != null) {
                footerView.post {
                    addFooterView()
                }
            } else {
                hiAdapter.addFooterView(footerView)
            }

        }

        private fun getFooterView(): View {
            if (footerView == null) {
                footerView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_footer_loading, this@HiRecyclerView, false)
            }
            return footerView!!
        }

        private fun findLastVisibleItem(recyclerView: RecyclerView): Int {
            return when (val layoutManager = recyclerView.layoutManager) {
                is LinearLayoutManager -> {
                    layoutManager.findLastVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    layoutManager.findLastVisibleItemPositions(null)[0]
                }
                else -> {
                    -1
                }
            }
        }
    }

    fun enableLoadMore(callback: () -> Unit, prefetchSize: Int) {
        if (adapter !is HiAdapter) {
            return
        }
        loadScrollerListener = LoadScrollerListener(prefetchSize, callback)
        addOnScrollListener(loadScrollerListener!!)
    }

    fun isLoading(): Boolean {
        return isLoadingMore
    }

    fun loadFinished(success: Boolean) {
        if (adapter !is HiAdapter) {
            return
        }
        isLoadingMore = false
        val hiAdapter = adapter as HiAdapter
        if (!success) {
            footerView?.let {
                if (footerView!!.parent != null) {
                    hiAdapter.removeFooterView(footerView!!)
                }
            }
        } else {
            //noting
        }
    }

    fun disableLoadMore() {
        if (adapter !is HiAdapter) {
            return
        }
        val hiAdapter = adapter as HiAdapter
        footerView?.let {
            if (footerView!!.parent != null) {
                hiAdapter.removeFooterView(footerView!!)
            }
        }
        loadScrollerListener?.let {
            removeOnScrollListener(loadScrollerListener!!)
            loadScrollerListener = null
            footerView = null
            isLoadingMore = false
        }
    }
}