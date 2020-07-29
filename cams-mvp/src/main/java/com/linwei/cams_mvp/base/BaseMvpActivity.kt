package com.linwei.cams_mvp.base

import com.linwei.cams.base.activity.BaseActivity
import com.linwei.cams.di.component.AppComponent
import com.linwei.cams_mvp.di.component.BaseMvpActivityComponent
import com.linwei.cams_mvp.di.component.DaggerBaseMvpActivityComponent
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
        val mvpActivityComponent: BaseMvpActivityComponent =
            DaggerBaseMvpActivityComponent.builder()
                .appComponent(appComponent) //提供application
                .build()

        setUpActivityChildComponent(mvpActivityComponent)
    }

    override fun provideLifecycleSubject(): Subject<ActivityEvent>? = mLifecycleSubject

    /**
     * 提供给 {@link Activity}实现类，进行{@code appComponent}依赖
     * @param mvpActivityComponent [BaseMvpActivityComponent]
     */
    abstract fun setUpActivityChildComponent(mvpActivityComponent: BaseMvpActivityComponent?)

    override fun onDestroy() {
        super.onDestroy()

        mPresenter.onDestroy()
    }

}
