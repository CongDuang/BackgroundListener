package com.mps.backgroundlistener

import android.app.Application

/**
 * @description BackgroundListener
 * @author msp
 * @time 2021/11/14
 */
class MyApplication:Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化
        BackgroundListener.init(this)
    }

}