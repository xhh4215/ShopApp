package com.example.shopapp.fragment.home

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.common.ui.view.loadUrl
import com.example.hi.item.HiDataItem
import com.example.library.utils.HiDisplayUtil
import com.example.shopapp.R
import com.example.shopapp.model.Subcategory

class GridItem(val list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, RecyclerView.ViewHolder>(list) {
    override fun onBindData(holder: RecyclerView.ViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter = GridAdapter(context,list)
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = HiDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(Color.WHITE)
        return gridView

    }

    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private lateinit var inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = inflater.inflate(R.layout.layout_home_op_grid_item, parent, false)
            return object : RecyclerView.ViewHolder(view) {


            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val subcategory = list[position]
            holder.itemView.findViewById<ImageView>(R.id.item_image)
                .loadUrl(subcategory.subcategoryIcon)
            holder.itemView.findViewById<TextView>(R.id.item_title).text =
                subcategory.subcategoryName
            holder.itemView.setOnClickListener {

            }

        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

}