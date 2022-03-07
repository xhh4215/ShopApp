package com.example.shopapp.degarde

import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.route.HiRoute
import com.example.common.ui.component.HiBaseActivity
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityDegradeGlobalBinding

/***
 * @author 栾桂明
 * @desc 全局的统一错误页面
 */
@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : HiBaseActivity<ActivityDegradeGlobalBinding>() {
    @Autowired
    @JvmField
    var degrade_title: String? = null

    @Autowired
    @JvmField
    var degrade_desc: String? = null

    @Autowired
    @JvmField
    var degrade_action: String? = null


    override fun layoutIdRes(): Int {
        return R.layout.activity_degrade_global
    }


    override fun initData() {

        HiRoute.inject(this)
        dataBinding.emptyView.setIcon(R.string.if_unexpected1)
        if (degrade_title != null) {
            dataBinding.emptyView.setTitle(degrade_title!!)
        }
        if (degrade_desc != null) {
            dataBinding.emptyView.setDesc(degrade_desc!!)
        }
        if (degrade_action != null) {
            dataBinding.emptyView.setHelperAction(listener = {
                Toast.makeText(this, "错误解决方法", Toast.LENGTH_SHORT).show()
                HiRoute.startActivity4Browser(url = degrade_action!!)
            })
        }
        dataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
    }


}