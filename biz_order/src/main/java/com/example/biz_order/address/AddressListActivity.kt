package com.example.biz_order.address

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.biz_order.R
import com.example.biz_order.databinding.ActivityAddressListBinding
import com.example.biz_order.model.Address
import com.example.common.route.RouteFlag
import com.example.common.ui.component.HiBaseActivity
import com.example.hi.empty.EmptyView
import com.example.hi.item.HiAdapter
import com.example.library.utils.HiStatusBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


//@AndroidEntryPoint
@Route(path = "/address/list", extras = RouteFlag.FLAG_LOGIN)
class AddressListActivity : HiBaseActivity<ActivityAddressListBinding>() {
//    @Inject
//    lateinit var emptyView: EmptyView
    private val viewModel: AddressViewModel by viewModels()


    override fun initData() {
        HiStatusBar.setStatusBar(this, true, translucent = false)
        initView()
        viewModel.queryAddressList().observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                bindData(it)
            } else {
                showHideEmptyView(true)
            }
        })
    }

    private fun bindData(list: List<Address>) {
        val items = arrayListOf<AddressItem>()
        for (address in list) {
            items.add(newAddressItem(address))
        }
        val hiAdapter = dataBinding.recyclerView.adapter as HiAdapter
        hiAdapter.clearItems()
        hiAdapter.addItems(items, true)
    }

    private fun newAddressItem(address: Address): AddressItem {
        return AddressItem(address, supportFragmentManager, itemClickCallback = { address ->
            val intent = Intent()
            intent.putExtra("result", address)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }, removeItemCallback = { address, addressItem ->
            viewModel.deleteAddress(addressId = address.id).observe(this@AddressListActivity,
                Observer { success ->
                    if (success) {
                        addressItem.removeItem()
                    }
                })
        }, viewModel = viewModel)
    }

    private fun initView() {
        dataBinding.navBar.setNavListener(View.OnClickListener { onBackPressed() })
        dataBinding.navBar.addRightTextButton(R.string.nav_add_address, R.id.nav_id_add_address)
            .setOnClickListener {
                showAddEditingDialog()
            }
        dataBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerView.adapter = HiAdapter(this)
        dataBinding.recyclerView.adapter?.registerAdapterDataObserver(adapterDataObserver)
    }

    private fun showAddEditingDialog() {
        val addEditingDialog = AddEditingDialogFragment.newInstance(null)
        addEditingDialog.setSavedAddressListener(object :
            AddEditingDialogFragment.onSavedAddressListener {
            override fun onSavedAddress(address: Address) {
                val hiAdapter: HiAdapter? = dataBinding.recyclerView.adapter as HiAdapter?
                hiAdapter?.addItemAt(0, newAddressItem(address), true)
            }
        })
        addEditingDialog.show(supportFragmentManager, "add_address")
    }

    private val adapterDataObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            showHideEmptyView(false)
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            dataBinding.recyclerView.post {
                if (dataBinding.recyclerView.adapter!!.itemCount <= 0) {
                    showHideEmptyView(true)
                }
            }
        }
    }

    private fun showHideEmptyView(showEmptyView: Boolean) {
        dataBinding.recyclerView.isVisible = !showEmptyView
//        emptyView.isVisible = showEmptyView
//        if (emptyView.parent == null && showEmptyView) {
//            dataBinding.rootLayout.addView(emptyView)
//        }
    }

    override fun onDestroy() {
        dataBinding.recyclerView?.adapter?.unregisterAdapterDataObserver(adapterDataObserver)
        super.onDestroy()
    }

    override fun layoutIdRes(): Int {
        return R.layout.activity_address_list
    }
}