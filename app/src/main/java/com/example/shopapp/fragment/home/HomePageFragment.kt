package com.example.shopapp.fragment.home

import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.common.ui.component.HiBaseFragment
import com.example.hi.bottom.HiTabBottomLayout
import com.example.hi.common.IHiTabLayout
import com.example.hi.top.HiTabTopInfo
import com.example.library.restful.HiCallBack
import com.example.library.restful.HiResponse
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentHomeBinding
import com.example.shopapp.http.ApiFactory
import com.example.shopapp.http.api.HomeApi
import com.example.shopapp.model.TabCategory
import kotlinx.android.synthetic.main.fragment_home.*

class HomePageFragment : HiBaseFragment<FragmentHomeBinding>() {
    private val homeViewModel: HomeViewModel by viewModels()
    private var topTabSelectIndex: Int = 0
    private val selectTabIndex: Int = 0
    val topTabs = mutableListOf<HiTabTopInfo<Int>>()
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(layoutView.findViewById(R.id.view_pager))
        queryTabList()
    }

    private fun queryTabList() {
        homeViewModel.queryCategoryTabs().observe(viewLifecycleOwner, Observer {
            it?.let {
                updateUI(it)
            }
        })

    }


    private val onTabSelectedListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, proInfo, nextInfo ->

            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)
            }
        }


    private fun updateUI(data: List<TabCategory>) {
        //topTabs需要提到全局，
        //因为addOnPageChangeListener  在第一次设置的时候，他就绑定了一次的局部变量topTabs
        //而再次刷新时，并没有 重新设置PageChangeListener，但是第一次的topTabs已经被释放了。
        topTabs.clear()
        data.forEachIndexed { _, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo =
                HiTabTopInfo<Int>(tabCategory.categoryName, selectColor, defaultColor)
            topTabs.add(tabTopInfo)
        }
        val viewPager = view_pager
        val topTabLayout = top_tab_layout
        topTabLayout.inflatedInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[selectTabIndex])
        topTabLayout.addTabSelectedChangeListener(onTabSelectedListener)
        val adapter = if (viewPager.adapter == null) {
            val homePagerAdapter = HomePagerAdapter(
                childFragmentManager,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            )
            viewPager.adapter = homePagerAdapter
            viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                override fun onPageSelected(position: Int) {
                    if (position != topTabSelectIndex) {
                        topTabLayout.defaultSelected(topTabs[position])
                        topTabSelectIndex = position
                    }
                }
            })
            homePagerAdapter
        } else {
            viewPager.adapter as HomePagerAdapter
        }
        adapter.update(data)
    }

    inner class HomePagerAdapter(
        fm: FragmentManager,
        behavior: Int
    ) : FragmentPagerAdapter(fm, behavior) {
        private val tabs = mutableListOf<TabCategory>()
        private val fragments = SparseArray<Fragment>(tabs.size)
        override fun getItem(position: Int): Fragment {
            val categoryId = tabs[position].categoryId
            val categoryKey = categoryId.toInt()
            var fragment = fragments.get(categoryKey, null)
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(categoryKey, fragment)
            }
            return fragment
        }

        override fun getCount(): Int {
            return tabs.size
        }

        //判断一个fragment = `object`，刷新前后在viewpager中的位置有没有发生变化。
        //return PagerAdapter.POSITION_NONE,则暂时detach掉，并不是移除，待会还有可能可以复用
        //
        override fun getItemPosition(`object`: Any): Int {
            //需要判断刷新前后 两次fragment 在viewpager中的位置 有没有改变，如果改变了PagerAdapter.POSITION_NONE ,否则返回PagerAdapter.POSITION_UNCHANGED
            //是为了避免缓存数据 和 接口数据返回的顶部导航栏数据一样的情况，导致页面的fragment 会被先detach ,在attach,重复执行生命周期
            //同时还能兼顾 缓存数据返回的顶部导航栏 和接口返回的数据 不一致的情况。这个case你可以【构造假数据测试,在updateUI如果是缓存数据,则删除前两个元素。】

            //我就拿到了刷新之前 该位置对应的fragment对象
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)
            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()
        }

        fun update(list: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(list)
            notifyDataSetChanged()
        }

    }
}