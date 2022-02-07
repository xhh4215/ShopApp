package com.example.shopapp.route

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.ui.component.HiBaseActivity
import com.example.common.ui.component.HiBaseApplication
import com.example.shopapp.R
import com.example.shopapp.databinding.ActivityDegradeGlobalBinding

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