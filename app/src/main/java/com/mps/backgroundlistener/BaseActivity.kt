package com.mps.backgroundlistener

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.log

/**
 * @description My Application2
 * @author msp
 * @time 2021/11/12
 */
open class BaseActivity:AppCompatActivity() {

    protected var isOpenPassword = true
    protected var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BackgroundListener.get(this).addListener(object :BackgroundListener.Listener{
            override fun onBecameForeground() {
                // 开启了 密码验证 并且 已经登录了
                if (isOpenPassword && isLogin) {
                    Log.i("BaseActivity", "该显示弹窗了嗷")
                }
                Log.e("BaseActivity", "onBecameForeground:前台")
            }

            override fun onBecameBackground() {
                Log.e("BaseActivity", "onBecameForeground:后台")
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}