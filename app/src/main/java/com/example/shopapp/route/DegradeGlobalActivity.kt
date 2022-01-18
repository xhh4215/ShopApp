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
class DegradeGlobalActivity : HiBaseActivity() {
    @Autowired
    @JvmField
    var degrade_title: String? = null

    @Autowired
    @JvmField
    var degrade_desc: String? = null

    @Autowired
    @JvmField
    var degrade_action: String? = null

    private lateinit var deGardeDataBinding: ActivityDegradeGlobalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiRoute.inject(this)
        deGardeDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_degrade_global)
        deGardeDataBinding.emptyView.setIcon(R.string.if_unexpected1)
        if (degrade_title != null) {
            deGardeDataBinding.emptyView.setTitle(degrade_title!!)
        }
        if (degrade_desc != null) {
            deGardeDataBinding.emptyView.setDesc(degrade_desc!!)
        }
        if (degrade_action != null) {
            deGardeDataBinding.emptyView.setHelperAction(listener = {
                Toast.makeText(this, "错误解决方法", Toast.LENGTH_SHORT).show()
                HiRoute.startActivity4Browser(url = degrade_action!!)
            })
        }
        deGardeDataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
    }
}