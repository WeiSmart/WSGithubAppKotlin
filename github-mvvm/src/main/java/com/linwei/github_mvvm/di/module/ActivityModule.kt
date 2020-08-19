package com.linwei.github_mvvm.di.module

import com.linwei.cams.di.component.BaseActivitySubComponent
import com.linwei.github_mvvm.mvvm.ui.module.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/8/12
 * @Contact: linwei9605@gmail.com
 * @Github: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
@Module(subcomponents = [BaseActivitySubComponent::class])
interface ActivityModule {

    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity

}
