package com.linwei.github_mvvm.mvvm.ui.module.main

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.github.nukc.stateview.StateView
import com.linwei.cams.ext.isNotNullOrSize
import com.linwei.cams.ext.otherwise
import com.linwei.cams.ext.yes
import com.linwei.cams_mvvm.base.BaseMvvmFragment
import com.linwei.github_mvvm.R
import com.linwei.github_mvvm.databinding.FragmentDynamicBinding
import com.linwei.github_mvvm.mvvm.contract.main.DynamicContract
import com.linwei.github_mvvm.mvvm.model.bean.Event
import com.linwei.github_mvvm.mvvm.model.bean.Page
import com.linwei.github_mvvm.mvvm.model.ui.EventUIModel
import com.linwei.github_mvvm.mvvm.ui.adapter.ReceivedEventAdapter
import com.linwei.github_mvvm.mvvm.viewmodel.ConversionBean
import com.linwei.github_mvvm.mvvm.viewmodel.main.DynamicViewModel
import com.linwei.github_mvvm.utils.EventUtils
import kotlinx.android.synthetic.main.fragment_dynamic.*

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/8/12
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
class DynamicFragment : BaseMvvmFragment<DynamicViewModel, FragmentDynamicBinding>(),
    DynamicContract.View {

    private lateinit var mReceivedEventAdapter: ReceivedEventAdapter

    override fun obtainStateViewRoot(): View? = dynamic_list_root

    override fun provideContentViewId(): Int = R.layout.fragment_dynamic

    private var mPage: Page<List<Event>>? = null

    private var mPageCode: Int = 1

    override fun bindViewModel() {
        mViewModel?.mLifecycleOwner = viewLifecycleOwner

        mViewDataBinding?.let {
            it.viewModel = mViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun initLayoutView(rootView: View?) {
        mReceivedEventAdapter = ReceivedEventAdapter(mutableListOf())
        mReceivedEventAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        mReceivedEventAdapter.loadMoreModule.isAutoLoadMore = true   //自动加载
        dynamic_recycler.apply {
            layoutManager = LinearLayoutManager(mContext)
            adapter = mReceivedEventAdapter
        }
    }

    override fun initLayoutData() {
        mViewModel?.page?.observe(viewLifecycleOwner, Observer {
            it?.let {
                mPage = it
                mPageCode = it.next
                it.result.isNotNullOrSize().yes {
                    if (it.prev == -1) {
                        mReceivedEventAdapter.setNewInstance(
                            ConversionBean.eventConversionByEventUIModel(it)
                        )
                    } else {
                        mReceivedEventAdapter.addData(
                            ConversionBean.eventConversionByEventUIModel(
                                it
                            )
                        )
                    }

                    (it.next == it.last).yes {
                        mReceivedEventAdapter.loadMoreModule.loadMoreEnd()
                    }.otherwise {
                        mReceivedEventAdapter.loadMoreModule.loadMoreComplete()
                    }
                }
                if (dynamic_refresh.isRefreshing)
                    dynamic_refresh.isRefreshing = false
            }
        })
    }

    override fun initLayoutListener() {
        mStateView?.onRetryClickListener = object : StateView.OnRetryClickListener {
            override fun onRetryClick() {
                reloadData()
            }
        }

        mReceivedEventAdapter.setOnItemClickListener { adapter: BaseQuickAdapter<*, *>, view: View, position: Int ->
            //根据 `EventUIModel.actionType` 类型, 跳转到仓库详情。
            EventUtils.evenAction(mActivity, adapter.data[position] as EventUIModel)
        }

        mReceivedEventAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel?.toReceivedEvent(mPageCode)
        }

        dynamic_refresh.setOnRefreshListener {
            mReceivedEventAdapter.loadMoreModule.isEnableLoadMore = false
            mPageCode = 1
            mViewModel?.toReceivedEvent(mPageCode)
        }
    }

    override fun reloadData() {
        loadData()
    }

    override fun loadData() {
        dynamic_refresh.isRefreshing = true
        mPageCode = 1
        mViewModel?.toReceivedEvent(mPageCode)
    }
}
