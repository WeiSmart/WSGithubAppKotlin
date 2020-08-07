package com.linwei.cams_mvvm.global

import android.app.Application
import android.content.Context
import com.linwei.cams.base.lifecycle.AppLifecycles
import com.linwei.cams.ext.obtainAppComponent
import com.linwei.cams_mvvm.di.component.DaggerMvvmComponent
import com.linwei.cams_mvvm.di.component.MvvmComponent
import timber.log.Timber

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/7/10
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
class AppLifecycleMvvmImpl : AppLifecycles {

    private lateinit var mMvvmComponent: MvvmComponent

    override fun attachBaseContext(context: Context) {
        Timber.i("AppLifecycleMvvmImpl to attachBaseContext!")
    }

    override fun onCreate(application: Application) {
        this.mMvvmComponent = DaggerMvvmComponent
            .builder()
            .appComponent(obtainAppComponent())
            .build()
        mMvvmComponent.inject(this)
    }

    override fun onTerminate(application: Application) {
        Timber.i("AppLifecycleMvvmImpl to onTerminate!")
    }

    override fun onLowMemory(application: Application) {
        Timber.i("AppLifecycleMvvmImpl to onLowMemory!")
    }

    override fun onTrimMemory(level: Int) {
        Timber.i("AppLifecycleMvvmImpl to onTrimMemory!")
    }
}