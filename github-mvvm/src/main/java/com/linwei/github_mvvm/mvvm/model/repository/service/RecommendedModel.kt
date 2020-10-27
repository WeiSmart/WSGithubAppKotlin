package com.linwei.github_mvvm.mvvm.model.repository.service

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.linwei.cams.ext.isNotNullOrEmpty
import com.linwei.cams.ext.no
import com.linwei.cams.ext.string
import com.linwei.cams.http.callback.LiveDataCallBack
import com.linwei.cams.http.config.ApiStateConstant
import com.linwei.cams_mvvm.http.DataMvvmRepository
import com.linwei.cams_mvvm.mvvm.BaseModel
import com.linwei.github_mvvm.R
import com.linwei.github_mvvm.mvvm.contract.main.RecommendedContract
import com.linwei.github_mvvm.mvvm.model.AppGlobalModel
import com.linwei.github_mvvm.mvvm.model.api.Api
import com.linwei.github_mvvm.mvvm.model.api.service.RepoService
import com.linwei.github_mvvm.mvvm.model.bean.Page
import com.linwei.github_mvvm.mvvm.model.bean.Repository
import com.linwei.github_mvvm.mvvm.model.bean.TrendingRepoModel
import com.linwei.github_mvvm.mvvm.model.repository.db.LocalDatabase
import com.linwei.github_mvvm.mvvm.model.repository.db.dao.ReposDao
import com.linwei.github_mvvm.mvvm.model.repository.db.entity.ReceivedEventEntity
import com.linwei.github_mvvm.utils.GsonUtils
import timber.log.Timber
import javax.inject.Inject

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/8/12
 * @Contact: linwei9605@gmail.com"d
 * @Follow: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
class RecommendedModel @Inject constructor(
        private val appGlobalModel: AppGlobalModel,
        dataRepository: DataMvvmRepository
) : BaseModel(dataRepository), RecommendedContract.Model {

    /**
     *  仓库服务接口
     */
    private val repoService: RepoService by lazy {
        dataRepository.obtainRetrofitService(RepoService::class.java)
    }

    /**
     * 仓库服务接口
     */
    private val reposDao: ReposDao by lazy {
        dataRepository.obtainRoomDataBase(LocalDatabase::class.java).reposDao()
    }

    override fun requestTrendData(
            owner: LifecycleOwner,
            languageType: String,
            since: String,
            observer: LiveDataCallBack<List<TrendingRepoModel>>
    ): LiveData<List<TrendingRepoModel>> {

        return repoService.getTrendDataAPI(true, Api.API_TOKEN, since, languageType).apply {
            observe(
                    owner,
                    object : LiveDataCallBack<List<TrendingRepoModel>>() {
                        override fun onSuccess(code: String?, data: List<TrendingRepoModel>?) {
                            super.onSuccess(code, data)
                            data?.let {
                                val entity = ReceivedEventEntity(
                                        id = 0,
                                        data = GsonUtils.toJsonString(it)
                                )
                                //userDao.insertReceivedEvent(entity)

                                observer.onSuccess(code, data)

                                Timber.i(" request Http=\"/trend/list\" Data Success~")
                            }
                        }

                        override fun onFailure(code: String?, message: String?) {
                            super.onFailure(code, message)
                            //queryReceivedEvent(owner, 0, observer)
                            Timber.i(" request Http=\"/trend/list\" Data Failed~")
                        }
                    })
        }
    }

    override fun requestUserRepository100StatusDao(
            owner: LifecycleOwner,
            forceNetWork: Boolean,
            page: Int,
            sort: String,
            per_page: Int,
            observer: LiveDataCallBack<Page<List<Repository>>>
    ): LiveData<Page<List<Repository>>> {
        val name: String? = appGlobalModel.userObservable.login
        name.isNotNullOrEmpty().no {
            observer.onFailure(ApiStateConstant.REQUEST_FAILURE, R.string.unknown_error.string())
            return@no
        }

        return repoService.getUserRepository100StatusDao(true, name!!, page, sort, per_page).apply {
            observe(
                    owner,
                    object : LiveDataCallBack<Page<List<Repository>>>() {
                        override fun onSuccess(code: String?, data: Page<List<Repository>>?) {
                            super.onSuccess(code, data)
                            data?.let {

                                observer.onSuccess(code, data)

                                Timber.i(" request Http=\"users/{user}/repos\" Data Success~")
                            }
                        }

                        override fun onFailure(code: String?, message: String?) {
                            super.onFailure(code, message)

                            observer.onFailure(code, message)
                            Timber.i(" request Http=\"users/{user}/repos\" Data Failed~")
                        }
                    })
        }

    }

    override fun requestGetStarredRepos(
            owner: LifecycleOwner,
            forceNetWork: Boolean,
            page: Int,
            sort: String,
            per_page: Int,
            observer: LiveDataCallBack<Page<List<Repository>>>
    ): LiveData<Page<List<Repository>>> {
        val name: String? = appGlobalModel.userObservable.login
        name.isNotNullOrEmpty().no {
            observer.onFailure(ApiStateConstant.REQUEST_FAILURE, R.string.unknown_error.string())
            return@no
        }

        return repoService.getStarredRepos(true, name!!, page, sort, per_page).apply {
            observe(
                    owner,
                    object : LiveDataCallBack<Page<List<Repository>>>() {
                        override fun onSuccess(code: String?, data: Page<List<Repository>>?) {
                            super.onSuccess(code, data)
                            data?.let {

                                observer.onSuccess(code, data)

                                Timber.i(" request Http=\"users/{user}/starred\" Data Success~")
                            }
                        }

                        override fun onFailure(code: String?, message: String?) {
                            super.onFailure(code, message)

                            observer.onFailure(code, message)
                            Timber.i(" request Http=\"users/{user}/starred\" Data Failed~")
                        }
                    })
        }
    }
}