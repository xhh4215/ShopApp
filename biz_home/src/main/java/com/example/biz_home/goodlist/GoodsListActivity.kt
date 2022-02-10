package com.example.biz_home.goodlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_home.R
import com.example.biz_home.databinding.ActivityGoodsListBinding
import com.example.library.utils.HiStatusBar
import com.example.common.route.HiRoute

@Route(path = "/goods/list")
class GoodsListActivity : AppCompatActivity() {
    @Autowired
    @JvmField
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    private lateinit var goodListDataBinding: ActivityGoodsListBinding

    private val FRAGMENT_GOODS_LIST_TAG = "fragment_goods_list_tag"

    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        super.onCreate(savedInstanceState)
        goodListDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_list)
        HiRoute.inject(this)
        goodListDataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        goodListDataBinding.title.text = categoryTitle
        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_GOODS_LIST_TAG)
        if (fragment == null) {
            fragment = GoodListFragment.newInstance(categoryId, subcategoryId)
        }
      var fm =   supportFragmentManager.beginTransaction()
        if(!fragment.isAdded){
           fm.add(R.id.container,fragment,FRAGMENT_GOODS_LIST_TAG)
        }
        fm.show(fragment).commitNowAllowingStateLoss()
    }
}