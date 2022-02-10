package com.example.biz_home.category

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.biz_home.R
import com.example.biz_home.api.CategoryApi
import com.example.biz_home.databinding.FragmentCategoryBinding
import com.example.common.ui.component.HiBaseFragment
import com.example.hi.empty.EmptyView
import com.example.common.ui.view.loadUrl
import com.example.hi.bottom.HiTabBottomLayout
import com.example.hi.slider.HiSliderView
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.common.http.ApiFactory
import com.example.biz_home.model.Subcategory
import com.example.biz_home.model.TabCategory
import com.example.common.route.HiRoute

class CategoryFragment : HiBaseFragment<FragmentCategoryBinding>() {
    private var emptyView: EmptyView? = null
    private val viewModel: CategoryViewModel by viewModels()
    private var rootContainer: RelativeLayout? = null
    private var loadingView: View? = null
    private var sliderView: HiSliderView? = null
    private val subcategoryListCache = mutableMapOf<String, List<Subcategory>>()
    private val SPAN_COUNT = 3
    private val decoration = CategoryItemDecoration({ position ->
        subcategoryList[position].groupName
    }, SPAN_COUNT)

    override fun getLayoutId(): Int {
        return R.layout.fragment_category

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(rootContainer)

        rootContainer = layoutView.findViewById(R.id.root_container)
        loadingView = layoutView.findViewById(R.id.content_loading)
        sliderView = layoutView.findViewById(R.id.slider_view)
        loadingView?.visibility = View.VISIBLE
        queryCategoryList()
    }

    private val spanSizeLookUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName: String = subcategoryList[position].groupName
            val nextGroupName: String? =
                if (position + 1 < subcategoryList.size) subcategoryList[position + 1].groupName else null

            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                //当前位置和 下一个位置 不再同一个分组
                //1 .要拿到当前组 position （所在组）在 groupSpanSizeOffset 的索引下标
                //2 .拿到 当前组前面一组 存储的 spansizeoffset 偏移量
                //3 .给当前组最后一个item 分配 spansize count

                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                val lastGroupOffset = if (size <= 0) 0
                else if (indexOfKey >= 0) {
                    //说明当前组的偏移量记录，已经存在了 groupSpanSizeOffset ，这个情况发生在上下滑动，
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)
                } else {
                    //说明当前组的偏移量记录，还没有存在于 groupSpanSizeOffset ，这个情况发生在 第一次布局的时候
                    //得到前面所有组的偏移量之和
                    groupSpanSizeOffset.valueAt(size - 1)
                }
                //          3       -     (6     +    5               % 3  )第几列=0  ，1 ，2
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT
                if (indexOfKey < 0) {
                    //得到当前组 和前面所有组的spansize 偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }
            return spanSize
        }
    }

    private fun queryCategoryList() {
        viewModel.queryCategoryList()?.observe(viewLifecycleOwner, {
            if (it == null) {
                showEmptyView()
            } else {
                onQueryCategoryListSuccess(it)
            }
        })
    }

    private fun onQueryCategoryListSuccess(data: List<TabCategory>) {
        if (!isAlive) return
        emptyView?.visibility = View.GONE
        loadingView?.visibility = View.GONE
        sliderView?.visibility = View.VISIBLE
        sliderView?.bindMenuView(itemCount = data.size, onBindView = { holder, position ->
            val category = data[position]
            holder.findViewById<TextView>(R.id.menu_item_title)?.text = category.categoryName
        }, onItemClick = { holder, position ->
            val category = data[position]
            val categoryId = category.categoryId

            if (subcategoryListCache.containsKey(categoryId)) {
                onQuerySubcategoryListSuccess(subcategoryListCache[categoryId]!!)
            } else {
                querySubcategoryList(categoryId)
            }
        })

    }

    private val subcategoryList = mutableListOf<Subcategory>()
    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)
    private val groupSpanSizeOffset = SparseIntArray()


    private fun querySubcategoryList(categoryId: String) {
        viewModel?.querySubcategoryList(categoryId).observe(viewLifecycleOwner, {
            if (it != null) {
                onQuerySubcategoryListSuccess(it)
                if (!subcategoryListCache.containsKey(categoryId)) {
                    subcategoryListCache[categoryId] = it
                }
            }
        })
    }

    private fun onQuerySubcategoryListSuccess(data: List<Subcategory>) {
        if (!isAlive) return
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)

        if (layoutManager.spanSizeLookup != spanSizeLookUp) {
            layoutManager.spanSizeLookup = spanSizeLookUp
        }
        sliderView?.bindContentView(
            itemCount = data.size,
            itemDecoration = decoration,
            layoutManager = layoutManager,
            onBindView = { holder, position ->
                val subcategory = data[position]
                holder.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subcategory.subcategoryIcon)
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    subcategory.subcategoryName
            },
            onItemClick = { holder, position ->
                //是应该跳转到类目的商品列表页的
                val subcategory = data[position]
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(requireContext(), bundle, HiRoute.Destination.GOODS_LIST)
            }
        )

    }


    private fun showEmptyView() {
        if (!isAlive) return
        if (emptyView == null) {
            emptyView = EmptyView(requireContext())
            emptyView?.setIcon(R.string.if_empty3)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })

            emptyView?.setBackgroundColor(Color.WHITE)
            emptyView?.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            rootContainer!!.addView(emptyView)
        }

        rootContainer!!.visibility = View.GONE
        sliderView!!.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }


}