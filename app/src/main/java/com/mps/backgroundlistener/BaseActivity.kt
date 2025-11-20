package com.mps.backgroundlistener

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * @description My Application2
 * @author msp
 * @time 2021/11/12
 */
open class BaseActivity : AppCompatActivity() {

    protected var isOpenPassword = true
    protected var isLogin = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BackgroundListener.get(this).addListener(object : BackgroundListener.Listener {
            override fun onBecameForeground() {
                // 开启了 密码验证 并且 已经登录了
                if (isOpenPassword && isLogin) {
                    AlertDialog.Builder(this@BaseActivity)
                        .setCancelable(false)
                        .setMessage("这里应该弹出一个Activity 或者 全局的 Dialog 只有输入密码后，才能继续使用app")
                        .setPositiveButton("确定") { dialog, which ->
                            dialog.dismiss()
                        }
                        .setNeutralButton("取消") { dialog, which ->
                            dialog.dismiss()
                        }
                        .show()
                }
                Log.e("BaseActivity", "onBecameForeground:前台")
            }

            override fun onBecameBackground() {
                Log.e("BaseActivity", "onBecameBackground:后台")
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