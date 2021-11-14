# 前言

> 给自己看系列

想要实现，当我们自己App被切换到后台 或 用户点击 HOME 按键后，再回到我们自己的App中时，弹出一个全局弹窗，或者跳转到需要的界面去。关键代码上传到了[github](https://github.com/CongDuang/BackgroundListener)中，如果需要请自取。代码主要借鉴了[《Android app前后台切换监听》](https://www.jianshu.com/p/22a0145fece6)

# 主要实现

通过让`Application`注册`ActivityLifecycleCallbacks`，并在`onActivityResumed` 和 `onActivityPaused`判断到底是当前的`Activity`是刚刚完成哪一个生命周期。

关键代码:

- 说明：`CHECK_DELAY`是为了正常的`Activity`跳转时，第一`Activity`的`onActivityPaused`中的 `check`这个Handler还未执行时，就被第二个`Activity`在`onActivityResumed`移除掉了。这样就可以达到只有在切换App或点击HOME键时才会触发监听器的效果。
- `CHECK_DELAY`的数值需要根据具体的开发情况自己去修改。


```kotlin
private val listeners = CopyOnWriteArrayList<Listener>()

private var check: Runnable? = null

var isForeground = false
var isPaused = true

companion object {
  const val CHECK_DELAY = 600L
}

private val handler = Handler(Looper.myLooper()!!)

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
      listeners.last().onBecameBackground()
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
```

