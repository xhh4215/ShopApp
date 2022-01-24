package com.example.shopapp.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.common.ui.component.HiBaseFragment
import com.example.hi.bottom.HiTabBottomLayout
import com.example.hi.top.HiTabTopInfo
import com.example.hi.top.HiTabTopLayout
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.shopapp.R
import com.example.shopapp.fragment.home.HomeTabFragment
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.HomeApi
import com.example.shopapp.model.TabCategory

class HomePageFragment : HiBaseFragment() {
    private var topTabSelectIndex: Int = 0
    private val selectTabIndex: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_home;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(layoutView.findViewById(R.id.view_pager))
        queryTabList()
    }

    private fun queryTabList() {
        ApiFactory.create(HomeApi::class.java).queryTabList()
            .enqueue(object : HiCallBack<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    val data = response.data
                    if (response.successful() && data != null) {
                        updateUI(data!!)
                    }
                }

                override fun onFailed(throwable: Throwable) {
                }

            })
    }

    private fun updateUI(data: List<TabCategory>) {
        //小心处理
        if (!isAlive) return
        val topTabs = mutableListOf<HiTabTopInfo<Int>>()
        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo =
                HiTabTopInfo<Int>(tabCategory.categoryName, selectColor, defaultColor)
            topTabs.add(tabTopInfo)
        }
        val topTabLayout = layoutView.findViewById<HiTabTopLayout>(R.id.top_tab_layout)
        val viewPager = layoutView.findViewById<ViewPager>(R.id.view_pager)
        topTabLayout.inflatedInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[selectTabIndex])
        topTabLayout.addTabSelectedChangeListener { index, proInfo, nextInfo ->
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)
            }
        }
        viewPager.adapter = HomePagerAdapter(
            childFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            data
        )
        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                if (position != topTabSelectIndex) {
                    topTabLayout.defaultSelected(topTabs[position])
                    topTabSelectIndex = position
                }

            }

        })
    }

    inner class HomePagerAdapter(
        fm: FragmentManager,
        behavior: Int,
        val tabs: List<TabCategory>
    ) : FragmentPagerAdapter(fm, behavior) {
        val fragments = SparseArray<Fragment>(tabs.size)
        override fun getCount(): Int {
            return tabs.size
        }

        override fun getItem(position: Int): Fragment {
            var fragment = fragments.get(position, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(position, fragment)
            }
            return fragment
        }

    }
}