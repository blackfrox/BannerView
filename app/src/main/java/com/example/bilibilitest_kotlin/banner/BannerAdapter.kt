package com.example.bilibilitest_kotlin.banner

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

/**
 * Created by Administrator on 2017/8/30/030.
 */
class BannerAdapter(val mList: List<ImageView>,val itemClick:() -> Unit): PagerAdapter() {

//    private val listener: ItemClickListener?=null

//     interface ItemClickListener{
//        fun click()
//    }
    override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
       return view==`object`
    }

    override fun getCount(): Int =Int.MAX_VALUE

    override fun instantiateItem(container: ViewGroup?, position: Int): Any {
        //对viewPager页号求摸取出view列表中要显示的项
        var pos=position%mList.size
        if (pos<0){
            pos+=mList.size
        }
        val v=mList[pos]
        v.scaleType=ImageView.ScaleType.CENTER
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛异常
        val vp=v.parent

        vp?.let { val parent=vp as ViewGroup
                    parent.removeView(v)}
        v.setOnClickListener {
//            listener?.let { listener.click() }
            itemClick()
        }
        container?.addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
        //Warning:不要在这里调用removeView
    }

}