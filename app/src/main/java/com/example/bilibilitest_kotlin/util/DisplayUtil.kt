package com.example.bilibilitest_kotlin.util

import android.content.Context

/**
 * Created by Administrator on 2017/8/29/029.
 */
object DisplayUtil {

    fun dp2px(context: Context,dpValue: Float): Int{
        val scale=context.resources.displayMetrics.density
        return (dpValue/scale+0.5f).toInt()
    }

}