package com.siddhartho.trendingrepositories.utils

import android.content.Context
import android.widget.Toast

fun Context.showToast(messageRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, getString(messageRes), duration).show()
}

object Constants {
    const val BASE_URL = "https://ghapi.huchen.dev/"
}