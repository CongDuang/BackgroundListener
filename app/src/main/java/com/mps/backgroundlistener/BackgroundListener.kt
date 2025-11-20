package com.mps.backgroundlistener

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.Exception
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @description 是否为后台监听器，用来监听app是否被切换到后台
 * @author msp
 * @time 2021/11/12
 */
class BackgroundListener : Application.ActivityLifecycleCallbacks {

    var isForeground = true
    var isPaused = true

    private val handler = Handler(Looper.myLooper()!!)

    private val listeners = CopyOnWriteArrayList<Listener>()

    private var check: Runnable? = null

    /**
     * 添加监听器
     */
    fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    /**
     * 移除监听器
     */
    fun removeListener(listener: Listener) {
        listeners.remove(listener);
    }

    /**
     * 清除全部监听器
     */
    fun removeAllListener() {
        listeners.clear()
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        isPaused = false
        // 之前是否在后台
        val wasBackground = !isForeground
        isForeground = true
        if (check != null) {
            handler.removeCallbacks(check!!)
        }
        if (wasBackground) {
            try {
                listeners.last().onBecameForeground()
            } catch (e: Exception) {
                Log.e(TAG, "onBecameForeground: ", e)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        isPaused = true
        if (check != null) {
            handler.removeCallbacks(check!!)
        }
        check = Runnable {
            if (isForeground && isPaused) {
                isForeground = false
                try {
                    listeners.last().onBecameBackground()
                } catch (e: Exception) {
                    Log.e(TAG, "onBecameBackground: ", e)
                }
            }

        }
        handler.postDelayed(check!!, CHECK_DELAY)
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    companion object {
        const val TAG = "BackgroundListener"
        const val CHECK_DELAY = 200L
        var instance: BackgroundListener? = null

        /**
         * 初始化，请在Application onCreate中初始化
         */
        fun init(application: Application): BackgroundListener {
            if (instance == null) {
                instance = BackgroundListener()
                application.registerActivityLifecycleCallbacks(instance)
            }
            return instance!!
        }

        /**
         * [Deprecated] 和 init方法作用相同，被废弃
         */
        fun get(application: Application) =
            if (instance == null) {
                init(application)
            } else {
                instance!!
            }

        /**
         * 通过上下文[context]获取对象,可以在Activity onResume,onPause 生命周期中调用处理代码
         */
        fun get(context: Context) =
            if (instance == null) {
                val application = context.applicationContext
                if (application is Application) {
                    init(application)
                }
                throw IllegalStateException("后台监听器未初始化，因为上下文无法获取applicationContext")
            } else {
                instance!!
            }

        /**
         * 获取对象
         */
        fun get() = instance
    }

    /**
     * 监听回调
     */
    interface Listener {

        /**
         * 成为前台程序
         */
        fun onBecameForeground()

        /**
         * 成为后台程序
         */
        fun onBecameBackground()

    }

}