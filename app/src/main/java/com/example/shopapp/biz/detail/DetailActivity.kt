package com.example.shopapp.biz.detail

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.common.ui.component.HiBaseActivity
import com.example.common.ui.view.EmptyView
import com.example.hi.item.HiAdapter
import com.example.hi.item.HiDataItem
import com.example.library.utils.HiStatusBar
import com.example.shopapp.R
import com.example.shopapp.biz.account.AccountManager
import com.example.shopapp.databinding.ActivityDetailBinding
import com.example.shopapp.fragment.home.GoodItem
import com.example.shopapp.model.DetailModel
import com.example.shopapp.model.GoodsModel
import com.example.shopapp.model.selectPrice
import com.example.shopapp.route.HiRoute

@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity<ActivityDetailBinding>() {
    private lateinit var viewModel: DetailViewModel
    private var emptyView: EmptyView? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null

    override fun layoutIdRes(): Int {
        return R.layout.activity_detail
    }

    override fun initData() {
        super.initData()
        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, translucent = true)
        HiRoute.inject(this)
        assert(!TextUtils.isEmpty(goodsId)) { " goodsId must bot be null" }
        initView()
        preBindData()
        queryDetailData()
    }

    private fun queryDetailData() {
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }


    private fun initView() {
        dataBinding.actionBack.setOnClickListener {
            onBackPressed()
        }
        dataBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        dataBinding.recyclerView.adapter = HiAdapter(this)
        dataBinding.recyclerView.addOnScrollListener(TitleScrollListener(callback = {
            dataBinding.titleBar.setBackgroundColor(it)
        }))
    }


    private fun preBindData() {
        if (goodsModel == null) return
        val hiAdapter = dataBinding.recyclerView.adapter as HiAdapter
        hiAdapter.addItemAt(
            0,
            HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice),
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }


    private fun bindData(detailModel: DetailModel) {
        dataBinding.recyclerView.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        val hiAdapter = dataBinding.recyclerView.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice),
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        dataItems.add(
            CommentItem(detailModel)
        )
        dataItems.add(
            ShopItem(detailModel)
        )
        dataItems.add(
            GoodsAttrItem(detailModel)
        )
        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }
        detailModel.similarGoods?.let {
            dataItems.add(SimilarTitleItem())
            it.forEach {
                dataItems.add(GoodItem(it, false))
            }
        }
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
        updateFavoriteActionFace(detailModel.isFavorite)
        updateOrderActionFace(detailModel)
    }

    private fun updateOrderActionFace(detailModel: DetailModel) {
        dataBinding.actionOrder.text = "${
            selectPrice(
                detailModel.groupPrice,
                detailModel.marketPrice
            )
        }" + getString(R.string.detail_order_action)
        //点击  现在先不写
    }

    private fun updateFavoriteActionFace(favorite: Boolean) {
        dataBinding.actionFavorite.setOnClickListener {
            toggleFavorite()
        }
        dataBinding.actionFavorite.setTextColor(
            ContextCompat.getColor(
                this,
                if (favorite) R.color.color_dd2 else R.color.color_999
            )
        )
    }

    private fun toggleFavorite() {
        if (!AccountManager.isLogin()) {
            AccountManager.login(this, Observer { loginSuccess ->
                if (loginSuccess) {
                    toggleFavorite()
                }
            })
        } else {
            dataBinding.actionFavorite.isClickable = false
            viewModel.toggleFavorite().observe(this, Observer { success ->
                if (success != null) {
                    //网络成功
                    updateFavoriteActionFace(success)
                    val message =
                        if (success) getString(R.string.detail_favorite_success) else getString(R.string.detail_cancel_favorite)
                    showToast(message)
                } else {
                    //网络失败
                }
                dataBinding.actionFavorite.isClickable = true
            })
        }

    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })

            dataBinding.rootContainer.addView(emptyView)
        }

        dataBinding.recyclerView.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

}