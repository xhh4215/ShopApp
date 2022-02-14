package com.example.biz_order.address

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.example.biz_order.R
import com.example.biz_order.model.Address
import com.example.hi.icfont.IconFontTextView
import com.example.hi.item.HiDataItem
import com.example.hi.item.HiViewHolder
import com.example.library.utils.HiRes
import kotlinx.android.synthetic.main.activity_address_list_item.*
import org.w3c.dom.Text

class AddressItem(
    var address: Address,
    val fm: FragmentManager,
    val removeItemCallback: (Address, AddressItem) -> Unit,
    val itemClickCallback: (Address) -> Unit,
    val viewModel: AddressViewModel
) : HiDataItem<Address, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.findViewById<TextView>(R.id.user_name)?.text = address.receiver
        holder.findViewById<TextView>(R.id.user_phone)?.text = address.phoneNum
        holder.findViewById<TextView>(R.id.user_address)?.text =
            "${address.province} ${address.city} ${address.area}"
        holder.findViewById<TextView>(R.id.edit_address)?.setOnClickListener {
            val dialog = AddEditingDialogFragment.newInstance(address)
            dialog.setSavedAddressListener(object :
                AddEditingDialogFragment.onSavedAddressListener {
                override fun onSavedAddress(newAddress: Address) {
                    address = newAddress
                    refreshItem()
                }
            })
            dialog.show(fm, "edit_address")
        }

        holder.findViewById<IconFontTextView>(R.id.delete)?.setOnClickListener {
            AlertDialog.Builder(context).setMessage(HiRes.getString(R.string.address_delete_title))
                .setNegativeButton(R.string.address_delete_cancel, null)
                .setPositiveButton(R.string.address_delete_ensure) { dialog, which ->
                    dialog.dismiss()
                    removeItemCallback(address, this)
                }.show()
        }
        holder.itemView.setOnClickListener {
            itemClickCallback(address)
        }

        holder.findViewById<TextView>(R.id.default_address)?.setOnClickListener {
            viewModel.checkedPosition = position
            viewModel.checkedAddressItem?.refreshItem()
            viewModel.checkedAddressItem = this
            refreshItem()
        }


        val select = viewModel.checkedAddressItem == this && viewModel.checkedPosition == position
        holder.findViewById<TextView>(R.id.default_address)
            ?.setTextColor(HiRes.getColor(if (select) R.color.color_dd2 else R.color.color_999))
        holder.findViewById<TextView>(R.id.default_address)?.text =
            HiRes.getString(if (select) R.string.address_default else R.string.set_default_address)
        holder.findViewById<TextView>(R.id.default_address)
            ?.setCompoundDrawablesWithIntrinsicBounds(
                if (select) R.drawable.ic_checked_red else 0,
                0,
                0, 0
            )
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.activity_address_list_item
    }
}