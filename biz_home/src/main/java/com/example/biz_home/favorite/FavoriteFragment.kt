package com.example.biz_home.favorite

import com.example.biz_home.R
import com.example.biz_home.databinding.FragmentFavoriteBinding
import com.example.common.ui.component.HiBaseFragment

class FavoriteFragment : HiBaseFragment<FragmentFavoriteBinding>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite;
    }
}