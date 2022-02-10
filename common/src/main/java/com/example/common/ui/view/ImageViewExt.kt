package com.example.common.ui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.media.Image
import android.text.TextUtils
import android.widget.ImageView
 import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.library.utils.HiViewUtil

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadUrl(url: String, callback: (Drawable) -> Unit) {
    //you cannot load url from destory activity
    if (HiViewUtil.isActivityDestroyed(context)) return
    Glide.with(context).load(url).into(object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            callback(resource)
        }
    })
}
@BindingAdapter(value = ["circleUrl"])
fun ImageView.loadCircleUrl(circleUrl: String?) {
    if (HiViewUtil.isActivityDestroyed(context) || TextUtils.isEmpty(circleUrl)) return
    Glide.with(this).load(circleUrl)
        .transform(CircleCrop()).into(this)
}

@BindingAdapter(value = ["url", "corner"], requireAll = false)
fun ImageView.loadCorner(url: String, corner: Int) {
    val transform = Glide.with(this).load(url).transform(CenterCrop())
    if (corner > 0) {
        RoundedCorners(corner)
    }
    transform.into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    Glide.with(this).load(url).transform().into(this)

}


class BorderTransform(private val borderWidth: Float, borderColor: Int) : CircleCrop() {
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.color = borderColor
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val halfWidth = outWidth / 2.toFloat()
        val halfHeight = outHeight / 2.toFloat()
        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfHeight.coerceAtMost(halfWidth) - borderWidth / 2,
            borderPaint
        )
        canvas.setBitmap(null)
        return transform
    }

}