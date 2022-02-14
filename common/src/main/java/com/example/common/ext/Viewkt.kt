package com.example.common.ext

import android.widget.Toast
import com.example.library.utils.AppGlobals

fun <T> T.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) = Toast.makeText(
    AppGlobals.get()!!,
    message, duration
).show()