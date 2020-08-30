package com.linwei.github_mvvm.mvvm.ui.module.login

import android.view.View
import com.linwei.cams_mvvm.base.BaseMvvmFragment
import com.linwei.github_mvvm.R
import com.linwei.github_mvvm.databinding.FragmentDynamicBinding
import com.linwei.github_mvvm.databinding.FragmentOauthLoginBinding
import com.linwei.github_mvvm.mvvm.contract.main.DynamicContract
import com.linwei.github_mvvm.mvvm.viewmodel.login.OAuthLoginViewModel
import com.linwei.github_mvvm.mvvm.viewmodel.main.DynamicViewModel

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/8/12
 * @Contact: linwei9605@gmail.com"
 * @Follow: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
class OAuthLoginFragment : BaseMvvmFragment<OAuthLoginViewModel, FragmentOauthLoginBinding>(),
    DynamicContract.View {

    override fun provideContentViewId(): Int = R.layout.fragment_oauth_login

    override fun bindViewModel() {
        mViewDataBinding?.let {
            it.viewModel = mViewModel
            it.lifecycleOwner = viewLifecycleOwner
        }
    }


    override fun initLayoutView(rootView: View?) {

    }

    override fun initLayoutData() {
    }

    override fun initLayoutListener() {
    }

    override fun reloadData() {
    }

    override fun loadData() {
    }

}