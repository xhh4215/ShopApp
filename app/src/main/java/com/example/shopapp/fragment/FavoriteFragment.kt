package com.example.shopapp.fragment

import com.example.common.ui.component.HiBaseFragment
import com.example.shopapp.R
import com.example.shopapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : HiBaseFragment<FragmentFavoriteBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite;
    }
}