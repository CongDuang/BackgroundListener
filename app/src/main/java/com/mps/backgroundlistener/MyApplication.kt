package com.mps.backgroundlistener

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.util.Log

/**
 * @description BackgroundListener
 * @author msp
 * @time 2021/11/14
 */
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 初始化
//        val processName = getProcessName(this, android.os.Process.myPid());
//        if (processName == this.packageName) {
            //initAppStatusListener()
//        }
        initAppStatusListener()

    }

    private fun initAppStatusListener() {
        BackgroundListener.init(this)
//            .addListener(object : BackgroundListener.Listener {
//            override fun onBecameForeground() {
//                Log.e("ForegroundCallbacks", "onBecameForeground:前台")
//                val intent = Intent(this@MyApplication,MainActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////                startActivity(intent)
//            }
//
//            override fun onBecameBackground() {
//                Log.e("ForegroundCallbacks", "onBecameForeground:后台")
//            }
//        })
    }

    private fun getProcessName(context: Context, pid: Int): String? {
        val activityManager = (context.getSystemService(Context.ACTIVITY_SERVICE)) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        if (runningAppProcesses.isNullOrEmpty()) {
            Log.e("getProcessName", "runningAppProcesses=$runningAppProcesses")
            return null
        }
        runningAppProcesses.forEach {
            if (it.pid == pid) {
                return it.processName
            }
        }
        return null
    }

}