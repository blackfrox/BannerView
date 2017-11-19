package com.example.bilibilitest_kotlin


import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.bilibilitest_kotlin.banner.BannerEntity
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        initView()
    }

    private fun initView() {
        val bannerList= mutableListOf<BannerEntity>()
        for (i in 0..2){
            bannerList.add(BannerEntity("1",R.mipmap.ic_launcher_round,"www"))
        }

         //bannerView使用
        bannerView.build(bannerList)
    }

}
