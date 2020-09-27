package com.linwei.cams.ext

import android.content.Context
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.linwei.cams.R
import com.linwei.cams.utils.ToastUtils
import java.io.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/6/2
 * @Contact linwei9605@gmail.com
 * @Follow https://github.com/WeiShuaiDev
 * @Description: 提供 View 扩展方法
 *-----------------------------------------------------------------------
 */

/**
 * ---------------------------------Toast---------------------------------
 */

/**
 *短时间 [Toast]
 * @param args [Array]
 */
fun Any.showShort(vararg args: String) {
    toastBuild().showShort(this.string(), args)
}

/**
 * 长时间 [Toast]
 *  @param args [Array]
 */
fun Any.showLong(vararg args: String) {
    toastBuild().showLong(this.string(), args)
}

/**
 * 安全模式短时间 [Toast]
 *  @param args [Array]
 */
fun Any.showShortSafe(vararg args: String) {
    toastBuild().showShortSafe(this.string(), args)
}

/**
 * 安全模式长时间 [Toast]
 *  @param args [Array]
 */
fun Any.showLongSafe(vararg args: String) {
    toastBuild().showLongSafe(this.string(), args)
}

/**
 * 获取ToastUtils
 */
fun toastBuild(): ToastUtils {
    return ToastUtils.Builder(ctx)
        //.setBgResource(R.drawable.shape_toast_background)
        .setMessageColor(R.color.colorGlobalBlack)
        .build()
}


/**
 * ---------------------------------Color---------------------------------
 */

/**
 * ResId颜色转换
 * @return color [Int]
 */
fun Int.color(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ctx.resources.getColor(this, null)
    } else {
        @Suppress("DEPRECATION")
        ctx.resources.getColor(this)
    }
}
/**
 * ---------------------------------String---------------------------------
 */
/**
 * [ResId] 转换为字符串 [String]
 * @param [ResId]
 * @return [String]
 */
fun Any.string(): String {
    return if (this is Int) {
        ctx.resources.getString(this)
    } else {
        this.toString()
    }
}

/**
 * [ResId] 转换为字符串 [String]
 * @param [ResId] 字符串Id, [args] 字符串格式化参数
 * @return [String]
 */
fun Any.string(vararg args: String): String {
    return if (this is Int) {
        ctx.resources.getString(this, *args)
    } else {
        this.toString()
    }
}


/**
 * 判断字符串是否为空
 * @param params [Array]
 */
fun isEmptyParameter(vararg params: String?): Boolean {
    for (p: String? in params)
        if (p.isNullOrEmpty() || p == "null" || p == "NULL") {
            return true
        }
    return false
}

/**
 * 判断字符串数组是否为空
 */
fun isEmptyArraysParameter(params: Array<out String?>): Boolean {
    for (p: String? in params)
        if (p.isNullOrEmpty() || p == "null" || p == "NULL") {
            return true
        }
    return false
}

/**
 * 判断字符串是否为空
 * @param params [String]
 */
fun String?.isNotNullOrEmpty(): Boolean {
    this?.let {
        if (it.isNotEmpty()) {
            if (it.toUpperCase(Locale.getDefault()) != "NULL") {
                return true
            }
        }
    }
    return false
}

/**
 * 至少包含大小写字母及数字中的两种
 * 是否包含
 * @return bool [Boolean]
 */
fun String.isMatchPwdFormat(): Boolean {
    if (this.isNotNullOrEmpty())
        return false

    var isDigit = false//定义一个boolean值，用来表示是否包含数字
    var isLetter = false//定义一个boolean值，用来表示是否包含字母
    for (i: Int in this.indices) {
        if (Character.isDigit(this[i])) {   //用char包装类中的判断数字的方法判断每一个字符
            isDigit = true
        } else if (Character.isLetter(this[i])) {  //用char包装类中的判断字母的方法判断每一个字符
            isLetter = true
        }
    }
    val regex = "^[a-zA-Z0-9]{6,20}$"
    return isDigit && isLetter && this.matches(regex.toRegex())
}

/**
 * 手机号码数字间添加空格 按照 xxx xxxx xxxx 的格式
 * @return [String]
 */
fun String.phoneFormat(): String {
    var phone: String = this
    if (this.isNotNullOrEmpty() && this.checkPhonePattern() && this.length >= 11) {
        phone = phone.substring(0, 3) + " " + phone.substring(3, 7) + " " + phone.substring(7)
    }
    return phone
}


/***
 * 检查手机号码是否正确
 * @return [String]
 */
fun String.checkPhonePattern(): Boolean {
    val phonePattern = "^1[3-9]\\d{9}$"
    return isNullOrEmpty().not() && matches(phonePattern.toRegex())
}

/**
 * 检查中文
 * @return [Boolean]
 */
fun String.checkNamePattern(): Boolean {
    val namePattern = "^[\\u4E00-\\u9FA5·.]+$"
    return matches(namePattern.toRegex())
}


/**
 * 获取url对应的域名
 * @return [String]
 */
fun String.getDomain(): String {
    var j = 0
    var startIndex = 0
    var endIndex = this.length - 1
    for (i in this.indices) {
        if (this[i] == '/') {
            j++
            if (j == 2)
                startIndex = i
            else if (j == 3)
                endIndex = i
        }

    }
    return this.substring(startIndex + 1, endIndex)
}

/**
 * 序列化对象
 */
fun Any.fromBean(): String {
    try {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        return base64(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return ""
}

/**
 * 反序列化对象
 */
fun String.toBean(): Any? {
    try {
        val bytes = Base64.decode(this, Base64.NO_WRAP)
        return ObjectInputStream(ByteArrayInputStream(bytes)).readObject()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

/**
 * ---------------------------------View---------------------------------
 */
/**
 * 扩展点击事件
 * @param listener [View.OnClickListener]
 * @return [View]
 */
fun View.onClick(listener: View.OnClickListener): View {
    setOnClickListener(listener)
    return this
}

/**
 * 点击事件处理
 * @param method [Method]
 * @param params  [String]
 * @param message [String]
 */
fun View.onClick(method: () -> Unit, message: Int = -1, vararg params: String?): View {
    setOnClickListener {
        checkClick({
            if (params.isNotEmpty() && isEmptyArraysParameter(params)) {
                if (message > 0)
                    message.showShort()
            } else {
                method()
            }
        }, {
            R.string.operate_often.showShort()
        })
    }
    return this
}


/**
 * 防止多次点击处理
 */
private val timer = Timer()
private val mPending: AtomicBoolean = AtomicBoolean(true)
fun checkClick(success: () -> Unit, failure: () -> Unit) {
    if (mPending.compareAndSet(true, false)) {
        success()
        //延时
        timer.schedule(object : TimerTask() {
            override fun run() {
                mPending.set(true)
            }
        }, 1000)
    } else {
        failure()
    }
}

/**
 * 扩展视图可见性
 * @param visible [Boolean]
 * @return [View]
 */
fun View.setVisible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * 设置字体颜色
 * @param text 文本[String]
 * @param textColor 文本颜色 [Int]
 * @param startIndex 起始位置 [Int]
 * @param endIndex  结束位置 [Int]
 */
fun TextView.setTextColorSpan(text: String, textColor: Int, startIndex: Int, endIndex: Int) {
    val span = SpannableString(text)
    span.setSpan(
        ForegroundColorSpan(ContextCompat.getColor(context!!, textColor)),
        startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    setText(span)
    movementMethod = LinkMovementMethod.getInstance()
}

/**
 *  设置字体大小
 * @param text 文本[String]
 * @param textColor 文本大小 [Float]
 * @param startIndex 起始位置 [Int]
 * @param endIndex  结束位置 [Int]
 */
fun TextView.setTextSizeSpan(text: String, textSize: Float, startIndex: Int, endIndex: Int) {
    val span = SpannableString(text)
    span.setSpan(
        AbsoluteSizeSpan(textSize.sp2px()),
        startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    setText(span)
    movementMethod = LinkMovementMethod.getInstance()
}

/**
 * 收起软键盘
 */
fun View.hideSoftKeyboard() {
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * 显示软键盘
 */
fun View.showSoftKeyboard() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, 0)
}


/**
 * ---------------------------------Size---------------------------------
 */
/**
 * DP-PX
 */
fun Float.dp2px(): Int {
    val scale: Float = ctx.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * PX-DP
 */
fun Float.px2dp(): Int {
    val scale: Float = ctx.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * SP-PX
 */
fun Float.sp2px(): Int {
    val scale: Float = ctx.resources.displayMetrics.scaledDensity
    return (this * scale + 0.5f).toInt()
}

/**
 * PX-SP
 */
fun Float.px2sp(): Int {
    val scale: Float = ctx.resources.displayMetrics.scaledDensity
    return (this / scale + 0.5f).toInt()
}

/**
 * 屏幕像素长度
 * @return  widthPixels [Int]
 */
fun getScreenWidthPixels(): Int = ctx.resources.displayMetrics.widthPixels


/**
 * 屏幕像素高度
 * @return  heightPixels [Int]
 */
fun getScreenHeightPixels(): Int = ctx.resources.displayMetrics.heightPixels




