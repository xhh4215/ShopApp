package com.example.biz_order

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_order.address.AddEditingDialogFragment
import com.example.biz_order.databinding.ActivityOrderBinding
import com.example.biz_order.model.Address
import com.example.common.ext.loadUrl
import com.example.common.route.HiRoute
import com.example.common.route.RouteFlag
import com.example.common.ui.component.HiBaseActivity
import com.example.library.utils.HiRes
import com.example.library.utils.HiStatusBar

@Route(path = "/order/main", extras = RouteFlag.FLAG_LOGIN)
class OrderActivity : HiBaseActivity<ActivityOrderBinding>() {
    @JvmField
    @Autowired
    var shopName: String? = null

    @JvmField
    @Autowired
    var shopLogo: String? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsName: String? = null

    @JvmField
    @Autowired
    var goodsImage: String? = null

    @JvmField
    @Autowired
    var goodsPrice: String? = null

    private val REQUEST_CODE_ADDRESS_LIST = 1000
    private val viewModel by viewModels<OrderViewModel>()
    override fun initData() {
        super.initData()
        HiStatusBar.setStatusBar(this, true, translucent = false)
        HiRoute.inject(this)
        initView()
        updateTotalPayPrice(dataBinding.amountView.getAmountValue())

        viewModel.queryMainAddress().observe(this) {
            updateAddress(it)
        }
    }

    private fun updateAddress(address: Address?) {
        val hasMainAddress = address != null && !TextUtils.isEmpty(address.receiver)
        dataBinding.addAddress.visibility = if (hasMainAddress) View.GONE else View.VISIBLE
        dataBinding.mainAddress.visibility = if (hasMainAddress) View.VISIBLE else View.GONE
        if (hasMainAddress) {
            dataBinding.userName.text = address!!.receiver
            dataBinding.userPhone.text = address.phoneNum
            dataBinding.userAddress.text = "${address.province} ${address.city} ${address.area}"
            dataBinding.userAddress.setOnClickListener {
                //地址列表页
                HiRoute.startActivity(
                    this,
                    destination = HiRoute.Destination.ADDRESS_PAGE,
                    requestCode = REQUEST_CODE_ADDRESS_LIST
                )
            }
        } else {
            dataBinding.userAddress.setOnClickListener {
                //弹窗 新增地址
                val addEditDialog = AddEditingDialogFragment.newInstance(null)
                addEditDialog.setSavedAddressListener(object :
                    AddEditingDialogFragment.onSavedAddressListener {
                    override fun onSavedAddress(address: Address) {
                        updateAddress(address)
                    }
                })
                addEditDialog.show(supportFragmentManager, "add_address")
            }
        }
    }

    private fun initView() {
        dataBinding.navBar.setNavListener { onBackPressed() }

        //店铺logo ,名称，商品主图 单价 和名称
        shopLogo?.apply { dataBinding.shopLogo.loadUrl(this) }
        dataBinding.shopTitle.text = shopName
        goodsImage?.apply { dataBinding.goodsImage.loadUrl(this) }
        dataBinding.goodsTitle.text = goodsName
        dataBinding.goodsPrice.text = goodsPrice

        //计数器
        dataBinding.amountView.setAmountValueChangedListener {
            updateTotalPayPrice(it)
        }

        //支付渠道
        dataBinding.channelWxPay.setOnClickListener(channelPayListener)
        dataBinding.channelAliPay.setOnClickListener(channelPayListener)

        //立即购买
        dataBinding.orderNow.setOnClickListener {
            showToast("not support for now")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotalPayPrice(amount: Int) {
        //amount* goodsPrice.contains("¥")
        // goodsPrice =10.08 ，x ￥
        dataBinding.totalPayPrice.text = String.format(
            HiRes.getString(
                R.string.free_transport,
                PriceUtil.calculate(goodsPrice, amount)
            )
        )

    }

    private val channelPayListener = View.OnClickListener {
        val aliPayChecked = it.id == dataBinding.channelAliPay.id
        dataBinding.channelAliPay.isChecked = aliPayChecked
        dataBinding.channelWxPay.isChecked = !aliPayChecked
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == REQUEST_CODE_ADDRESS_LIST && resultCode == Activity.RESULT_OK) {
            val address = data.getParcelableExtra<Address>("result")
            if (address != null) {
                updateAddress(address)
            }
        }
    }

    override fun layoutIdRes(): Int {
        return R.layout.activity_order
    }

}