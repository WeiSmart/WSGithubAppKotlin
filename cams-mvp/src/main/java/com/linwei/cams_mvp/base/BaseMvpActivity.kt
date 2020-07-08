package com.linwei.cams_mvp.base

import com.linwei.cams.base.activity.BaseActivity
import com.linwei.cams.di.component.AppComponent
import com.linwei.cams_mvp.di.component.BaseActivityComponent
import com.linwei.cams_mvp.di.component.DaggerBaseActivityComponent
import com.linwei.cams_mvp.lifecycle.ActivityRxLifecycle
import com.linwei.cams_mvp.mvp.BasePresenter
import com.linwei.cams_mvp.mvp.IModel
import com.linwei.cams_mvp.mvp.IView
import com.trello.rxlifecycle4.android.ActivityEvent
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import javax.inject.Inject

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/7/5
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description: `MVP` 架构 `Activity` 基类
 *-----------------------------------------------------------------------
 */
abstract class BaseMvpActivity<T : BasePresenter<IModel, IView>> : BaseActivity(),
    ActivityRxLifecycle {

    private var mLifecycleSubject: BehaviorSubject<ActivityEvent> = BehaviorSubject.create()

    @Inject
    lateinit var mPresenter: T

    override fun setUpActivityComponent(appComponent: AppComponent?) {
        val activityComponent: BaseActivityComponent = DaggerBaseActivityComponent.builder()
            .appComponent(appComponent) //提供application
            .build()

        setUpActivityChildComponent(activityComponent)
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent>? = mLifecycleSubject

    override fun onDestroy() {
        super.onDestroy()

        mPresenter.onDestroy()
    }

    /**
     * 提供给 {@link BaseMvpActivity}实现类，进行{@code appComponent}依赖
     * @param activityComponent [BaseActivityComponent]
     */
    abstract fun setUpActivityChildComponent(activityComponent: BaseActivityComponent?)


}
