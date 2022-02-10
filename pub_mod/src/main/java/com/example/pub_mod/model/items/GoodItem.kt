package com.example.pub_mod.model.items

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
 import android.view.Gravity
import android.view.View
import android.view.LayoutInflater.from
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
 import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder
import com.example.library.utils.HiDisplayUtil
import com.example.common.route.HiRoute
import com.example.pub_mod.R
import com.example.pub_mod.BR
import com.example.pub_mod.databinding.LayoutHomeGoodListItem1Binding
import com.example.pub_mod.databinding.LayoutHomeGoodListItem2Binding
import com.example.pub_mod.model.GoodsModel

open class GoodItem(val goodsModel: GoodsModel, val hotTab: Boolean) :
    HiDataItem<GoodsModel, GoodItem.GoodItemHolder>(goodsModel) {
    private val MAX_TAG_SIZE = 3
    override fun onBindData(holder: GoodItemHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.setVariable(BR.goodModel, goodsModel)
        val labelContainer = holder.itemView.findViewById<LinearLayout>(R.id.item_label_container)
        if (labelContainer != null) {
            if (!TextUtils.isEmpty(goodsModel.tags)) {
                labelContainer.visibility = View.VISIBLE
                val splits = goodsModel.tags.split(" ")
                for (index in splits.indices) {
                    val childCount = labelContainer.childCount
                    if (index > MAX_TAG_SIZE - 1) {
                        for (index in childCount - 1 downTo MAX_TAG_SIZE - 1) {
                            labelContainer.removeViewAt(index)
                        }
                        break
                    }
                    val labelView: TextView = if (index > labelContainer.childCount - 1) {
                        val view = createLabelView(context, index != 0)
                        labelContainer.addView(view)
                        view
                    } else {
                        labelContainer.getChildAt(index) as TextView
                    }

                    labelView.text = splits[index]
                }

            } else {
                labelContainer.visibility = View.GONE
            }

        }
        if (!hotTab) {
            val margin = HiDisplayUtil.dp2px(2f)
            val layoutParams = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = hiAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = hiAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                layoutParams.rightMargin = margin
            } else {
                layoutParams.leftMargin = margin
            }
            holder.itemView.layoutParams = layoutParams
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("goodsId", goodsModel.goodsId)
            bundle.putParcelable("goodsModel", goodsModel)
            HiRoute.startActivity(context, bundle, HiRoute.Destination.DETAIL_MAIN)
        }
    }


    private fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelTextView = TextView(context)
        labelTextView.setTextColor(ContextCompat.getColor(context, R.color.color_e75))
        labelTextView.setBackgroundResource(R.drawable.shape_goods_label)
        labelTextView.textSize = 11f
        labelTextView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtil.dp2px(18f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtil.dp2px(5f) else 0
        params.gravity = Gravity.CENTER_VERTICAL
        labelTextView.layoutParams = params
        return labelTextView
    }


    override fun onCreateViewHolder(parent: ViewGroup): GoodItemHolder? {
        val inflater = from(parent.context)
        return if (hotTab) {
            val binding = LayoutHomeGoodListItem1Binding.inflate(inflater, parent, false)
            GoodItemHolder(binding)
        } else {
            val binding = LayoutHomeGoodListItem2Binding.inflate(inflater, parent, false)
            GoodItemHolder(binding)
        }
    }


    override fun getSpanSize(): Int {
        return if (hotTab) super.getSpanSize() else 1
    }

    class GoodItemHolder(val binding: ViewDataBinding) : HiViewHolder(binding.root)

}