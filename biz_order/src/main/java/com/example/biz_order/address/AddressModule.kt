package com.example.biz_order.address

import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.example.biz_order.R
import com.example.hi.empty.EmptyView
import com.example.library.utils.HiRes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object AddressModule {
    @Provides
    @ActivityScoped
    fun newEmptyView(@ActivityContext context: Context): EmptyView {
        val emptyView = EmptyView(context)
        emptyView.setBackgroundColor(Color.WHITE)
        emptyView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        emptyView.setIcon(R.string.list_empty)
        emptyView.setDesc(HiRes.getString(R.string.list_empty_desc))
        return emptyView
    }
}