package com.example.hi.item
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
/***
 * 通用的adapter加载使用的数据item
 * @param <DATA>  当前item对应的数据bean
 * @param <VH> 当前item对应的ViewHolder
 */
abstract class HiDataItem<DATA, VH : RecyclerView.ViewHolder>(data: DATA) {

    private lateinit var adapter: HiAdapter

    var mData: DATA? = null

    init {
        mData = data
    }

    /***
     * 当前item的数据绑定的操作
     * @param position 当前的item所处的位置
     */
    abstract fun onBindData(holder: RecyclerView.ViewHolder, position: Int)

    /****
     * 当前的item 对应的布局资源文件id
     */
    open fun getItemLayoutRes(): Int {
        return -1
    }

    /***
     * 当前item使用的布局View
     */
    open fun getItemView(parent: ViewGroup): View? {
        return null
    }

    fun setAdapter(adapter: HiAdapter) {
        this.adapter = adapter
    }

    /***
     * 刷新item
     */
    fun refreshItem() {
        adapter.refreshItem(this)
    }

    /***
     * 删除item
     */
    fun removeItem() {
        adapter.removeItem(this)
    }

    /***
     *当前item在列表中占据几列
     */
    fun setSpanSize(): Int {
        return 0
    }
}