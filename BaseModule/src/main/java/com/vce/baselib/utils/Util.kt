package com.vce.baselib.utils

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.Service
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.tencent.mmkv.MMKV
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern


object Util {
    lateinit var mApp: Application
    private var mToast: Toast? = null
    private var screenWidth = 0
    private var screenHeight = 0
    private var isToastShowing = false
    private var mainHandler: Handler? = null
    private var toastMsg: String? = null
    private var lastedTime = 0L

    @JvmStatic
    fun init(app: Application?) {
        if (app == null) return
        mApp = app
        screenWidth = mApp.resources.displayMetrics.widthPixels;
        screenHeight = mApp.resources.displayMetrics.heightPixels;
        MMKV.initialize(app)
    }

    fun getContext(): Context {
        return mApp
    }

    @JvmStatic
    fun toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
        if (!isRunningForeground()) return
        var cur = System.currentTimeMillis()
        if (msg == toastMsg && (cur - lastedTime < 2000)) return
        synchronized(Any::class.java) {
            lastedTime = cur
            if (Looper.myLooper() != Looper.getMainLooper()) {
                if (mainHandler == null) mainHandler = Handler(Looper.getMainLooper())
                mainHandler?.post {
                    show(msg, time)
                }
            } else {
                show(msg, time)
            }
        }
    }

    private fun show(msg: String, time: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(mApp, msg, time)
        } else {
            mToast?.setText(msg)
        }
        toastMsg = msg
        mToast?.show()
    }
//    private var toastCallback=object :Toast.Callback(){
//        override fun onToastShown() {
//            isToastShowing=true
//        }
//
//        override fun onToastHidden() {
//            isToastShowing=false
//        }
//    }

    @JvmStatic
    fun log(msg: String) {
        Log.d("vnews",msg)
    }

    @JvmStatic
    fun logE(err: String) {
        Log.e("vnews",err)
    }

    /**
     * ???????????????
     */
    fun matchMobile(mobile: String?): Boolean {
        if (mobile.isNullOrEmpty()) return false
        var pattern = Pattern.compile("^1\\d{10}$")
        val matcher = pattern.matcher(mobile)
        return matcher.matches()
    }

    /**
     * ???????????? 1 0 -1
     */
    fun compare(a: Int?, b: Int?): Int {
        val one = a ?: 0
        val two = b ?: 0
        if (one > two) {
            return 1
        } else if (one == two) {
            return 0
        } else {
            return -1
        }
    }

    /**
     * ???????????? String ???long??????
     */
    fun compare(a: String?, b: String?): Int {
        var one = 0f
        var two = 0f
        try {
            one = a?.toFloat() ?: 0f
            two = b?.toFloat() ?: 0f
            if (one > two) {
                return 1
            } else if (one == two) {
                return 0
            } else {
                return -1
            }
        } catch (e: Exception) {
        }
        return 0
    }

    /**
     * ??????string???float????????????
     */
    fun reduce(small: String?, big: String?): String {
        var s = 0f
        var b = 0f
        try {
            s = small?.toFloat() ?: 0f
            b = big?.toFloat() ?: 0f
            return "${s - b}"

        } catch (e: Exception) {
        }
        return ""
    }

    /** ????????????????????????  */
    @JvmStatic
    fun getStatusHeight(context: Context): Int {
        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val `object` = clazz.newInstance()
            val height = clazz.getField("status_bar_height")[`object`].toString().toInt()
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    @JvmStatic
    fun getScreenWidth() = screenWidth

    @JvmStatic
    fun getScreenHeight() = screenHeight


    /** ?????????????????????????????????GridView?????????????????? ???????????????????????????Item??????  */
    @JvmStatic
    fun getImageItemWidth(activity: Activity): Int {
        val screenWidth = activity.resources.displayMetrics.widthPixels
        val densityDpi = activity.resources.displayMetrics.densityDpi
        var cols = screenWidth / densityDpi
        cols = if (cols < 3) 3 else cols
        val columnSpace = (2 * activity.resources.displayMetrics.density).toInt()
        return (screenWidth - columnSpace * (cols - 1)) / cols
    }
    /**
     * ???????????????????????????
     * @param mContext
     * @param className??????Service.class.getName();
     * @return
     */
    public fun isServiceRunning(mContext: Context, className: String): Boolean {
        var isRunning = false;
        var activityManager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var seviceList = activityManager.getRunningServices(200)  //200:????????????service????????????????????????????????????????????????????????????????????????Serice
        if (seviceList.size <= 0) {
            return false;
        }
        seviceList.forEachIndexed { index, runningServiceInfo ->
            com.vce.baselib.log("service == ${runningServiceInfo.service.className}    check = $className")
            if (runningServiceInfo.service.className == className) {
                return true
            }
        }
        return false
    }
    /**
     * html?????? ???????????????
     */
    @JvmStatic
    fun getHtmlTextNoImage(src:CharSequence?): Spanned {
        return HtmlCompat.fromHtml(src?.toString()?:"", HtmlCompat.FROM_HTML_MODE_COMPACT)
    }

    /**
     * ??????SDCard????????????
     */
    @JvmStatic
    fun existSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * ?????????????????????????????????
     */
    @JvmStatic
    fun getScreenPix(activity: Activity): DisplayMetrics? {
        val displaysMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displaysMetrics)
        return displaysMetrics
    }

    /** dp???px  */
    fun dp2px(dpVal: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getContext().resources.displayMetrics).toInt()
    }

    /**
     * ????????????????????????????????????  99%
     */
    @JvmStatic
    fun hasVirtualNavigationBar(context: Context): Boolean {
        var hasSoftwareKeys = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)
            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels
            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)
            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels
            hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }
        return hasSoftwareKeys
    }

    @JvmStatic
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        var hasNavigationBar = false
        val rs = context.resources
        val id: Int = rs.getIdentifier("config_showNavigationBar", "bool", "android")
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id)
        }
        try {
            val systemPropertiesClass = Class.forName("android.os.SystemProperties")
            val m = systemPropertiesClass.getMethod("get", String::class.java)
            val navBarOverride = m.invoke(systemPropertiesClass, "qemu.hw.mainkeys") as String
            if ("1" == navBarOverride) {
                hasNavigationBar = false
            } else if ("0" == navBarOverride) {
                hasNavigationBar = true
            }
        } catch (e: java.lang.Exception) {
        }
        return hasNavigationBar
    }

    private const val NAVIGATION = "navigationBarBackground"

    // ??????????????????View??????????????????????????????????????????????????????
    @JvmStatic
    fun isNavigationBarExist(activity: Activity): Boolean {
//        if((RomUtils.checkIsHuaweiRom() && isHasNavigationBar(activity)) || (RomUtils.checkIsMiuiRom() && isMiuiFullScreen(activity)) || (RomUtils.checkIsVivoRom() && isVivoFullScreen(activity)) || RomUtils.checkIsHuaweiRom()){
        if(isAllScreenDevice(activity)){
            log("keyboard== ????????????")
            //????????????????????????????????? true ??????   navigationbarbackground
            val vp = activity.window.decorView as ViewGroup
            if (vp != null) {
                for (i in 0 until vp.childCount) {
                    var item = vp.getChildAt(i)
                    if (item.id != View.NO_ID && NAVIGATION == activity.resources.getResourceEntryName(item.id)) {
                        log("keyboard== ?????????  ???????????????")
                        return true
                    }
                }
//                val b = Util.hasVirtualNavigationBar(activity)
//                log("keyboard== ???????????? $b ")
//                val a = Util.checkDeviceHasNavigationBar(activity)
                log("keyboard== ???????????????????????? false ")
                return false
            }
            val a = Util.hasVirtualNavigationBar(activity)
            log("keyboard== ???????????? $a ")
            return a
        }else{
            log("keyboard== ???????????????")
            //?????? ???????????????
            val b = Util.hasVirtualNavigationBar(activity)
            log("keyboard== ???????????? $b ")
            return b
        }
//        //?????? ???????????????
//        val b = isHasNavigationBar(activity)
//        log("keyboard== ???????????? $b ")
//        val a = hasVirtualNavigationBar(activity)
//        log("keyboard== ????????????2 $a")
//        return b
//        log("hasNavigationBar first check = false")
        //???????????????
    }

    /**
     * ??????????????????????????????????????????
     * @return true ??????????????????false ???????????????
     */
    private fun isHuaWeiHideNav(context: Context) = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
        Settings.System.getInt(context.contentResolver, "navigationbar_is_min", 0)
    } else {
        Settings.Global.getInt(context.contentResolver, "navigationbar_is_min", 0)
    } != 0

    /**
     * ????????????????????????????????????
     * @return true ???????????????????????????false ?????????????????????????????????(NavigationBar)????????????false
     */
    private fun isMiuiFullScreen(context: Context) = Settings.Global.getInt(context.contentResolver, "force_fsg_nav_bar", 0) != 0

    /**
     * Vivo??????????????????????????????
     * @return true ???????????????????????????false ?????????????????????????????????(NavigationBar)????????????false
     */
    private fun isVivoFullScreen(context: Context) = Settings.Secure.getInt(context.contentResolver, "navigation_gesture_on", 0) != 0

    /**
     * ???????????????????????????????????????????????????????????????????????????
     * @return true ??????????????????????????????false ??????????????????????????????
     */
    private fun isHasNavigationBar(context: Context): Boolean {
        val windowManager: WindowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay

        val realDisplayMetrics = DisplayMetrics()
        display.getRealMetrics(realDisplayMetrics)
        val realHeight = realDisplayMetrics.heightPixels
        val realWidth = realDisplayMetrics.widthPixels

        val displayMetrics = DisplayMetrics()
        display.getMetrics(displayMetrics)
        val displayHeight = displayMetrics.heightPixels
        val displayWidth = displayMetrics.widthPixels

        // ???????????????????????????????????????????????? + ????????????????????????????????????????????????????????????????????????????????????????????????
        if (displayHeight > displayWidth) {
            if (displayHeight + getStatusHeight(context) < realHeight) return true
        } else {
            if (displayWidth + getStatusHeight(context) < realWidth) return true
        }

        return false
    }

    /**
     * ????????????????????????
     */
    @Volatile
    private var mHasCheckAllScreen = false

    @Volatile
    private var mIsAllScreenDevice = false

    fun isAllScreenDevice(context: Context): Boolean {
        if (mHasCheckAllScreen) {
            return mIsAllScreenDevice
        }
        mHasCheckAllScreen = true
        mIsAllScreenDevice = false
        // ?????? API 21????????????????????????????????????
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false
        }
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (windowManager != null) {
            val display = windowManager.defaultDisplay
            val point = Point()
            display.getRealSize(point)
            val width: Int
            val height: Int
            if (point.x < point.y) {
                width = point.x
                height = point.y
            } else {
                width = point.y
                height = point.x
            }
            if (height / width >= 1.97f) {
                mIsAllScreenDevice = true
            }
        }
        return mIsAllScreenDevice
    }


    /**
     * ??????????????????????????????????????????????????????????????????????????????????????????????????????????????????
     */
    @JvmStatic
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }

    /**
     * ?????????????????????????????????
     */
    fun isRunningForeground(): Boolean {
        val activityManager = getContext().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcesses = activityManager.runningAppProcesses
        if (runningAppProcesses.isNullOrEmpty()) return false
        var curPackageName = getContext().applicationInfo.packageName
        runningAppProcesses.forEach {
            if (curPackageName.equals(it.processName, true) && it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true
            }
        }
        return false
    }
    private fun clickSpan(click: () -> Unit) = object : ClickableSpan() {
        override fun onClick(widget: View) {
            click.invoke()
        }
    }

    /**
     * bitmap ???????????????
     */
    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray? {
        val output = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }
        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * ????????????????????????????????????
     */
    fun isTouchPointInView(view:View,x:Int,y:Int):Boolean{
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top+ view.measuredHeight
        return y in top..bottom && x>=left && y<=right
    }
    /**
     * ?????????????????????????????????
     */
    fun isTouchPointOnViewTop(view:View,y:Int):Boolean{
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val top = location[1]
        return y < top
    }
}