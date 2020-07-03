package com.linwei.cams.base.fragment

import android.os.Build
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.linwei.cams.R
import com.linwei.cams.base.holder.TopViewHolder
import com.linwei.cams.listener.OnTopLeftClickListener
import com.linwei.cams.listener.OnTopRightClickListener
import com.linwei.cams.utils.UIUtils

/**
 * @Author: WeiShuai
 * @Time: 2019/10/14
 * @Description: BaseFragmentWithTopAndStatus基类
 */
abstract class BaseFragmentWithTopAndStatus : BaseFragment() {
    lateinit var mTopViewHolder: TopViewHolder
    lateinit var mTopBarView: ViewGroup

    private var mOnTopLeftListener: OnTopLeftClickListener? = null//左边监听
    private var mOnTopRightListener: OnTopRightClickListener? = null //右边监听
    override fun getTopViewGroup(): ViewGroup? {
        return initTopBar()
    }

    private fun initTopBar(): ViewGroup? {
        if (getTopBarId() != -1) {
            mTopBarView = View.inflate(mActivity, getTopBarId(), null) as ViewGroup

            mTopViewHolder = TopViewHolder(mTopBarView)

            val titleId = getTitleId()
            if (titleId != 0) {
                setTopBarTitle(titleId)
            }

            //设置导航栏左边监听
            mOnTopLeftListener = getTopLeftListener()
//            mTopViewHolder.mBtnBack.setOnClickListener {
//                if (mOnTopLeftListener != null) {
//                    mOnTopLeftListener?.onTopClickListener()
//                } else {
//                    //关闭页面前收起软键盘
//                    mActivity.finish()
//                }
//            }

            //设置导航栏右边监听
            mOnTopRightListener = getTopRightListener()
//            mTopViewHolder.mBtnRefresh.setOnClickListener {
//                if (mOnTopRightListener != null) {
//                    mOnTopRightListener?.onTopRightClickListener()
//                }
//            }

            updateTopBarIsVisible()
        }
        return if (useImmersive()) {
            addStatusView(mTopBarView)
        } else {
            mTopBarView
        }
    }

    /**
     * 解决状态栏
     */
    private fun addStatusView(view: View?): ViewGroup {
        val linearLayout =
            View.inflate(mActivity, R.layout.base_content_layout, null) as LinearLayout

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val mStatusFillView = View(mActivity)
            var statusBarHeight: Int = getStatusbarHeight()
            if (statusBarHeight <= 0) {
                statusBarHeight = UIUtils.dp2px(mActivity, 25f)
            }

            val params =
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight)
            mStatusFillView.setBackgroundResource(getStatusColor())
            mStatusFillView.layoutParams = params
            linearLayout.addView(mStatusFillView)
        }

        if (view != null) {
            val params =
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            view.layoutParams = params
            linearLayout.addView(view)
        }

        return linearLayout
    }

    open fun getTopBarId(): Int = -1

    open fun getTitleId(): Int = -1

    open fun getStatusColor(): Int = R.color.colorPrimary

    /**
     * 是否使用沉浸式，默认是
     */
    open fun useImmersive() = false


    /**
     * 设置返回键显示或隐藏
     *
     * @param visible
     */
    open fun setTopBarBackVisible(): Boolean = false

    /**
     * 设置标题文本
     *
     * @param resId 资源id
     */
    fun setTopBarTitle(resId: Int) {
        if (resId > 0) {
            setTopBarTitle(getString(resId))
        }
    }

    /**
     * 设置标题文本
     *
     * @param title 字符
     */
    fun setTopBarTitle(title: String) {
        mTopViewHolder.mTvTitle.text = title
        updateTopBarIsVisible()
    }

    /**
     * 设置标题显示或隐藏
     */
    open fun getTopBarTitleVisible(): Boolean = true

    /**
     * 是否显示横
     */
    open fun setTopBarLineVisible(): Boolean = false

    /**
     * 设置右边点击事件
     */
    open fun getTopRightListener(): OnTopRightClickListener? = null

    /**
     * 左侧返回按钮的点击事件
     */
    open fun getTopLeftListener(): OnTopLeftClickListener? = null

    /**
     * 设置右边图片id
     */
    fun setTopBarRightImage(resId: Int) {
//        mTopViewHolder.mBtnRefresh.setImageResource(resId)
    }


    /**
     * 设置右边logo不可见
     */
    open fun setTopBarRightImageVisible(): Boolean = false

    /**
     * 设置右边标题
     */
    fun setTopBarRightText(resId: Int) {
        if (resId > 0) {
            setTopBarRightText(getString(resId))
        }
    }

    fun setTopBarRightText(text: String) {
        var title: String = text
        if (TextUtils.isEmpty(text)) {
            title = ""
        }
//        mTopViewHolder.mRightTitle.text = title
    }

    /**
     *设置右边标题可见
     */
    open fun setTopBarRightTextVisible(): Boolean = false

    /**
     * 刷新TopBar是否显示
     */
    private fun updateTopBarIsVisible() {
        mTopViewHolder.apply {
//            mIbChoose.setVisible(setTopBarRightImageVisible())
//            mIvBack.setVisible(setTopBarBackVisible())
//            mLine.setVisible(setTopBarLineVisible())
//            mRightTitle.setVisible(setTopBarRightTextVisible())
//            mTvTitle.setVisible(getTopBarTitleVisible())
        }
    }


}