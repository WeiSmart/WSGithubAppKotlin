package com.linwei.cams.base.activity

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import com.linwei.cams.R
import com.linwei.cams.base.holder.TopViewHolder
import com.linwei.cams.ext.hideSoftKeyboard
import com.linwei.cams.ext.string
import com.linwei.cams.listener.OnTopLeftClickListener
import com.linwei.cams.listener.OnTopRightClickListener

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2019/10/14
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description: [Activity] 导航栏基类
 *-----------------------------------------------------------------------
 */
abstract class BaseActivityWithTop : BaseActivity() {

    lateinit var mTopViewHolder: TopViewHolder

    override fun withTopContentView(): View {
        val view: View = View.inflate(this, provideContentViewId(), null)
        return addTopBarView(view)
    }

    /**
     * `TopBar` 配置处理，同时返回控件
     */
    private fun addTopBarView(view: View): View {
        val topBarView: View = View.inflate(this, getTopBarId(), null) as ViewGroup
        mTopViewHolder = TopViewHolder(topBarView)
        mTopViewHolder.mIncludeContent.addView(view)//添加内容区域的视图

        initTopBarEvent()

        return topBarView
    }

    /**
     * `TopBar` 增加事件处理,并通过 [OnTopLeftClickListener]、[OnTopRightClickListener]
     * 接口回调
     */
    private fun initTopBarEvent() {
        val topBarLeftListener: OnTopLeftClickListener? = fetchTopBarLeftListener()
        mTopViewHolder.mTvLeftTitle.setOnClickListener {
            if (topBarLeftListener != null) {
                topBarLeftListener.onTopClickListener()
            } else {
                //关闭页面前收起软键盘
                window.decorView.hideSoftKeyboard()
                finish()
            }
        }

        mTopViewHolder.mIbLeftImage.setOnClickListener {
            if (topBarLeftListener != null) {
                topBarLeftListener.onTopClickListener()
            } else {
                //关闭页面前收起软键盘
                window.decorView.hideSoftKeyboard()
                finish()
            }
        }

        // 获取头部右标题点击监听
        val topBarRightListener: OnTopRightClickListener? = fetchTopBarRightListener()
        mTopViewHolder.mIbRightImage.setOnClickListener {
            topBarRightListener?.onTopRightClickListener()
        }
        mTopViewHolder.mTvRightTitle.setOnClickListener {
            topBarRightListener?.onTopRightClickListener()
        }
    }

    /**
     * 导航栏布局`ResId`
     * @return [Int] 布局文件Id
     */
    abstract fun getTopBarId(): Int

    /**
     * 导航栏左侧点击事件
     * @return [OnTopLeftClickListener]
     */
    open fun fetchTopBarLeftListener(): OnTopLeftClickListener? {
        return null
    }

    /**
     * 导航栏右侧点击事件
     * @return [OnTopRightClickListener]
     */
    open fun fetchTopBarRightListener(): OnTopRightClickListener? {
        return null
    }

    /**
     * 设置 `TopBar`  标题文本
     * @param resId 资源id
     */
    fun setTopBarTitle(resId: Int) {
        if (resId > 0) {
            setTopBarTitle(resId.string())
        }
    }

    /**
     * 设置 `TopBar` 标题文本
     */
    fun setTopBarTitle(title:String){
        mTopViewHolder.mTvTitle.text = title
    }
    /**
     * 获取 `TopBar` 标题文本
     */
    fun getTopBarTvTitle(): TextView? {
        return mTopViewHolder.mTvTitle
    }


    /**
     * 设置 `TopBar` 右边 [Textview] 文本信息
     * @param resId [Int] 显示文本信息，默认显示`刷新`
     */
    fun setTopBarRightTitle(resId: Int = R.string.refresh) {
        mTopViewHolder.setRightTitleForId(resId)
    }

    /**
     * 设置 `TopBar` 右边 [ImageButton] 图片
     * @param resId [Int] 显示图片信息，默认显示`刷新图片箭头`
     */
    fun setTopBarRightImage(resId: Int = R.drawable.ic_refresh_black_24dp) {
        mTopViewHolder.setRightImageForId(resId)
    }


    /**
     * 设置 `TopBar` 左边 [Textview] 文本信息
     * @param resId [Int] 显示文本信息，默认显示 `退出`
     */
    fun setTopBarLeftTitle(resId: Int = R.string.setting) {
        mTopViewHolder.setLeftTitleForId(resId)
    }

    /**
     * 设置 `TopBar` 左边 [ImageButton] 图片
     * @param resId [Int] 显示图片信息，默认显示 `退出图片箭头`
     */
    fun setTopBarLeftImage(resId: Int = R.drawable.ic_refresh_black_24dp) {
        mTopViewHolder.setLeftImageForId(resId)
    }

    /**
     *  获取 `TopBar` 加载控件
     * @return [ProgressBar]
     */
    fun getTopBarLoadingView(): ProgressBar {
       return mTopViewHolder.mPbLoading
    }

    /**
     * 获取 `TopBar` 刷新控件
     * @return [ProgressBar]
     */
    fun getTopBarRefreshView(): ProgressBar {
        return mTopViewHolder.mPbRefresh
    }

}