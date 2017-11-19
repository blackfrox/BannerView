package com.example.bilibilitest_kotlin.banner

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.bilibilitest_kotlin.R
import com.example.bilibilitest_kotlin.util.DisplayUtil
import org.jetbrains.anko.find
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import java.util.concurrent.TimeUnit

/**
 * Created by Administrator on 2017/8/31/031.
 *
 */
class BannerView @JvmOverloads constructor(context: Context, attrs:AttributeSet?=null, defStyleAttr: Int=0)
    :RelativeLayout(context,attrs,defStyleAttr){

    val viewPager by lazy { find<ViewPager>(R.id.banner_viewPager) }
    val points by lazy { find<LinearLayout>(R.id.banner_points) }
//    val viewPager=find<ViewPager>(R.id.banner_viewPager)
//    val points=find<LinearLayout>(R.id.banner_points)

      var compositeSubscription: CompositeSubscription?=null

    //默认轮播时间
    private var delayTime=10

    lateinit var imageViewList:MutableList<ImageView>

    lateinit var bannerList: MutableList<BannerEntity>

    //选中显示Indicator
    private var selectRes=R.drawable.shape_dots_select
    //else
    private var unSelectRes =R.drawable.shape_dots_default

    //当前页下标
    private var currentPos=0

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_banner,this,true)
    }

    /**
     *设置轮播时间
     */
    fun delayTime(time: Int): BannerView {
        this.delayTime=time
        return this
    }

    /**
     * 设置points资源 Res
     */
    fun setPointRes(selectRes: Int,unSelectRes:Int){
        this.selectRes=selectRes
        this.unSelectRes =unSelectRes
    }

    /**
     * 图片轮播需要传入参数
     */
    fun build(list: List<BannerEntity>){
        destroy()
        if (list.size==0){
            this.visibility= View.GONE
            return
        }
        imageViewList= arrayListOf()
        bannerList= arrayListOf()
        bannerList.addAll(list)
        val pointSize=bannerList.size
        if (pointSize==2){
            bannerList.addAll(list)
        }
        //判断是否清空指示器点
        if (points.childCount!=0){
            points.removeAllViewsInLayout()
        }
        //初始化与个数相同的指示器点
        for (i in 0 until pointSize){
            val dot=View(context)
            dot.setBackgroundResource(unSelectRes)
            val params=LinearLayout.LayoutParams(
                    DisplayUtil.dp2px(context, 10F),
                    DisplayUtil.dp2px(context, 10F))
            params.leftMargin=10
            dot.layoutParams=params
            dot.isEnabled=false
            points.addView(dot)
        }
        points.getChildAt(0).setBackgroundResource(selectRes)

        for (i in 0 until pointSize){
            val mImageView=ImageView(context)
            Glide.with(context)
                    .load(bannerList[i].img)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bili_default_image_tv)
                    .dontAnimate()
                    .into(mImageView)
            imageViewList.add(mImageView)
        }

        //监听图片轮播，改变指示器状态
        viewPager.clearOnPageChangeListeners()
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{

            override fun onPageSelected(position: Int) {
                currentPos=position%pointSize
                for (i in 0 until points.childCount){
                    points.getChildAt(i).setBackgroundResource(unSelectRes)
                }
                points.getChildAt(currentPos).setBackgroundResource(selectRes)
            }

            override fun onPageScrollStateChanged(state: Int) {
                when(state){
                    ViewPager.SCROLL_STATE_IDLE ->{
                        if (isStopScroll){
                            startScroll()
                        }
                    }
                    ViewPager.SCROLL_STATE_DRAGGING ->{
                        stopScroll()
                        destroy()
                    }
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        })

        val bannerAdapter=BannerAdapter(imageViewList){
               //跳转事件
            }
        viewPager.adapter=bannerAdapter
        bannerAdapter.notifyDataSetChanged()

        //图片开始轮播
        startScroll()
    }

    private var isStopScroll=false

    private fun startScroll() {
        compositeSubscription= CompositeSubscription()

        isStopScroll=false
        val subscription=Observable.timer(delayTime.toLong(),TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
//                    if (isStopScroll){
//                        return@subscribe
//                    }
                    isStopScroll=true
                    viewPager.currentItem=viewPager.currentItem+1
                 }
        compositeSubscription!!.add(subscription)
    }

    private fun stopScroll(){
        isStopScroll=true
    }

    private fun destroy() {
        compositeSubscription?.unsubscribe()
    }
}