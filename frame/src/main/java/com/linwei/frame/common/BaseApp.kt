package com.linwei.frame.common

import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.linwei.frame.config.LibConfig
import com.linwei.frame.di.component.AppComponent
import com.linwei.frame.utils.AppLanguageUtils

/**
 * @Author: WeiShuai
 * @Time: 2019/10/14
 * @Description: BaseApplication基类
 */
abstract class BaseApp() : MultiDexApplication(), App {
    companion object {
        lateinit var mContext: Context
        lateinit var mInstance: BaseApp
    }

    override fun attachBaseContext(context: Context) {
        initLibConfig(context)
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(context, LibConfig.LANGUAGE))
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mContext = this
        AppLanguageUtils.setLanguage(this, LibConfig.LANGUAGE)
        LibConfig.initLib(this)
        initTool(this);
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppLanguageUtils.setLanguage(this, LibConfig.LANGUAGE)
    }

    /**
     * 初始化库
     */
    abstract fun initLibConfig(context: Context?)

    /**
     * 初始化工具
     */
    abstract fun initTool(baseApp: BaseApp)

}