package com.example.common.ui.component

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.R
import com.example.common.ui.view.EmptyView
import com.example.common.ui.view.HiRecyclerView
import com.example.hi.item.HiAdapter
import com.example.hi.item.HiDataItem
import com.example.hi.refresh.HiOverView
import com.example.hi.refresh.HiRefreshLayout
import com.example.hi.refresh.HiTextOverView
import com.example.hi.refresh.IHiRefresh

open class HiAbsListFragment : HiBaseFragment(), IHiRefresh.HiRefreshListener {
     var pageIndex = 1
    private lateinit var hiAdapter: HiAdapter
    private lateinit var refreshHeaderView: HiTextOverView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private var refreshLayout: HiRefreshLayout? = null
    private var recyclerView: HiRecyclerView? = null
    private var emptyView: EmptyView? = null
    private var loadingView: ContentLoadingProgressBar? = null

    companion object {
        const val PREFETCH_SIZE = 5
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_list
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.refreshLayout = layoutView.findViewById(R.id.refresh_layout)
        this.recyclerView = layoutView.findViewById(R.id.recycler_view)
        this.emptyView = layoutView.findViewById(R.id.empty_view)
        this.loadingView = layoutView.findViewById(R.id.content_loading)
        refreshHeaderView = HiTextOverView(context!!)
        refreshLayout?.setRefreshOverView(refreshHeaderView)
        refreshLayout?.setRefreshListener(this)
        layoutManager = createLayoutManager()
        hiAdapter = HiAdapter(context!!)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.adapter = hiAdapter
        emptyView?.visibility = View.GONE
        emptyView?.setIcon(R.string.list_empty)
        emptyView?.setDesc(getString(R.string.list_empty_desc))
        emptyView?.setButton(getString(R.string.list_empty_action)) {
            onRefresh()
        }
        loadingView?.visibility = View.VISIBLE
        pageIndex = 1


    }

    fun finishRefresh(dataItems: List<HiDataItem<*, out RecyclerView.ViewHolder>>?) {
        val success = dataItems != null && dataItems.isNotEmpty()
        val refresh = pageIndex == 1
        if (refresh) {
            loadingView?.visibility = View.GONE
            refreshLayout?.refreshFinished()
            if (success) {
                emptyView?.visibility = View.GONE
                hiAdapter.clearItems()
                hiAdapter.addItems(items = dataItems!!, true)
            } else {
                if (hiAdapter.itemCount <= 0) {
                    emptyView?.visibility = View.VISIBLE
                }
            }
        } else {
            if (success) {
                hiAdapter.addItems(dataItems!!, true)
            }
            recyclerView?.loadFinished(success)
        }
    }

    open fun createLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    fun enableLoadMore(callback: () -> Unit) {
        recyclerView?.enableLoadMore({
            if (refreshHeaderView.state == HiOverView.HiRefreshState.STATE_REFRESH) {
                recyclerView?.loadFinished(false)
                return@enableLoadMore
            }
            pageIndex++
            callback()
        }, PREFETCH_SIZE)

    }

    fun disableLoadMore() {
        recyclerView?.disableLoadMore()
    }

    @CallSuper
    override fun onRefresh() {
        if (recyclerView?.isLoading() == true) {
            refreshLayout?.post {
                refreshLayout?.refreshFinished()
            }
            return
        }
        pageIndex = 1
    }

    override fun enableRefresh(): Boolean {
        return true
    }
}