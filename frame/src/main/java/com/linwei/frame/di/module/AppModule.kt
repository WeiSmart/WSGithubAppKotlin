package com.linwei.frame.di.module

import android.app.Application
import androidx.fragment.app.FragmentManager
import com.linwei.frame.base.lifecycle.ActivityLifecycle
import com.linwei.frame.base.lifecycle.FragmentLifecycle
import com.linwei.frame.http.cache.Cache
import com.linwei.frame.http.cache.CacheType
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/5/22
 * @Contact: linwei9605@gmail.com
 * @Github: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */

@Module(includes = [AppBindsModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideFragmentLifecycles(): List<FragmentManager.FragmentLifecycleCallbacks> {
        return mutableListOf()
    }

    @Singleton
    @Provides
    fun provideExtras(cacheFactory: Cache.Factory): Cache<String, Any> {
        return cacheFactory.build(CacheType.extrasCacheType)
    }
}