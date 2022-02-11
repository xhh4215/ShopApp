package com.example.biz_search

import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_search.SearchViewModel.Companion.PAGE_INIT_INDEX
import com.example.biz_search.databinding.ActivitySearchBinding
import com.example.biz_search.view.GoodsSearchView
import com.example.biz_search.view.HistorySearchView
import com.example.biz_search.view.QuickSearchView
import com.example.common.ui.component.HiBaseActivity
import com.example.hi.empty.EmptyView
import com.example.hi.search.HiSearchView
import com.example.hi.search.SimpleTextWatcher
import com.example.library.utils.HiDisplayUtil
import com.example.library.utils.HiRes
import com.example.library.utils.HiStatusBar

@Route(path = "/search/main")
class SearchActivity : HiBaseActivity<ActivitySearchBinding>() {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var searchButton: Button
    private lateinit var searchView: HiSearchView
    private var emptyView: EmptyView? = null
    private var goodsSearchView: GoodsSearchView? = null
    private var quickSearchView: QuickSearchView? = null
    private var historyView: HistorySearchView? = null

    private var status = -1

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    override fun layoutIdRes(): Int {
        return R.layout.activity_search
    }

    override fun initData() {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        initTopBar()


    }

    private fun initTopBar() {
        dataBinding.navBar.setNavListener {
            onBackPressed()
        }
        searchButton =
            dataBinding.navBar.addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
        searchButton.setTextColor(HiRes.getColorStateList(R.color.color_nav_item_search))
        searchButton.setOnClickListener(searchClickListener)
        searchView = HiSearchView(this)
        searchView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            HiDisplayUtil.dp2px(38f)
        )
        searchView.setHintText(HiRes.getString(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)
        searchView.setDebounceTextChangedListener(debounceTextWatcher)
        dataBinding.navBar.setCenterView(searchView)
    }


    private val searchClickListener = View.OnClickListener {
        val keyword = searchView.editText?.text?.trim().toString()
        if (TextUtils.isEmpty(keyword)) return@OnClickListener
        doKeyWordSearch(KeyWord(null, keyword))
    }
    private val updateHistoryListener = View.OnClickListener {
        if (viewModel.keywords.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historyView?.bindData(viewModel.keywords!!)
        }
    }

    private val debounceTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            val hasContent = s != null && s.trim().isNotEmpty()
            searchButton.isEnabled = hasContent
            if (hasContent) {
                viewModel.quickSearch(s.toString())
                    .observe(this@SearchActivity, Observer { keywords ->
                        if (keywords.isNullOrEmpty()) {
                            updateViewStatus(STATUS_EMPTY)
                        } else {
                            updateViewStatus(STATUS_QUICK_SEARCH)
                            quickSearchView?.bindData(keywords) {
                                doKeyWordSearch(it)
                            }
                        }
                    })
            } else if (TextUtils.isEmpty(searchView.getKeyword())) {
                //当输入框内容 被回退删除,且没有高亮的关键词时，回退页面状态
                updateHistoryListener.onClick(null)
            }
        }
    }

    private fun doKeyWordSearch(keyWord: KeyWord) {
        //1.搜索框高亮 搜索词
        searchView.setKeyWord(keyWord.keyWord, updateHistoryListener)
        //2.keyword 存储起来
        viewModel.saveHistory(keyWord)
        //3. 发起 goodsSearch
        val kwClearIconView: View? = searchView.findViewById(R.id.id_search_clear_icon)
        kwClearIconView?.isEnabled = false
        viewModel.goodsSearch(keyWord.keyWord, true)
        //bugfix: 每次搜索都会执行这里，都会执行一次addobserver ，所以需要过滤下
        if (!viewModel.goodsSearchLiveData.hasObservers()) {
            viewModel.goodsSearchLiveData.observe(this, Observer { goodsList ->
                kwClearIconView?.isEnabled = true
                goodsSearchView?.loadFinished(!goodsList.isNullOrEmpty())
                val loadInit = viewModel.pageIndex == PAGE_INIT_INDEX;
                if (goodsList.isNullOrEmpty()) {
                    if (loadInit) {
                        updateViewStatus(STATUS_EMPTY)
                    }
                } else {
                    updateViewStatus(STATUS_GOODS_SEARCH)
                    goodsSearchView?.bindData(goodsList, loadInit)
                }
            })
        }
    }

    private fun updateViewStatus(newStatus: Int) {
        if (status == newStatus) return
        status = newStatus

        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (emptyView == null) {
                    emptyView = EmptyView(this)
                    emptyView?.setDesc(HiRes.getString(R.string.list_empty_desc))
                    emptyView?.setIcon(R.string.list_empty)
                }
                showView = emptyView
            }
            STATUS_QUICK_SEARCH -> {
                if (quickSearchView == null) {
                    quickSearchView = QuickSearchView(this)
                }
                showView = quickSearchView
            }
            STATUS_GOODS_SEARCH -> {
                if (goodsSearchView == null) {
                    goodsSearchView = GoodsSearchView(this)
                    goodsSearchView?.enableLoadMore({
                        val keyword = searchView.getKeyword()
                        if (TextUtils.isEmpty(keyword)) return@enableLoadMore
                        //分页
                        viewModel.goodsSearch(keyword!!, false)
                    }, 5)
                }
                showView = goodsSearchView
            }

            STATUS_HISTORY -> {
                if (historyView == null) {
                    historyView = HistorySearchView(this)
                    historyView!!.setOnCheckedChangedListener {
                        doKeyWordSearch(it)
                    }
                    historyView!!.setOnHistoryClearListener {
                        viewModel.clearHistory()
                        updateViewStatus(STATUS_EMPTY)
                    }
                }
                showView = historyView
            }
        }

        if (showView != null) {
            if (showView.parent == null) {
                dataBinding.container.addView(showView)
            }
            val childCount = dataBinding.container.childCount
            for (index in 0 until childCount) {
                val child = dataBinding.container.getChildAt(index)
                child.visibility = if (child == showView) View.VISIBLE else View.GONE
            }
        }
    }
}
