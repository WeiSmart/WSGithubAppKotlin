package com.linwei.github_mvvm.mvvm.model.api.service

import androidx.lifecycle.LiveData
import com.linwei.github_mvvm.mvvm.model.api.Api
import com.linwei.github_mvvm.mvvm.model.bean.CommitsComparison
import com.linwei.github_mvvm.mvvm.model.bean.Page
import com.linwei.github_mvvm.mvvm.model.bean.RepoCommit
import com.linwei.github_mvvm.mvvm.model.bean.RepoCommitExt
import retrofit2.http.*
import java.util.ArrayList

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/9/23
 * @Contact: linwei9605@gmail.com"d
 * @Follow: https://github.com/WeiShuaiDev
 * @Description:
 *-----------------------------------------------------------------------
 */
interface CommitService {
    /**
     * @param owner [String]
     * @param repo [String]
     * @param page [Int]
     * @param branch [String]
     *  @param per_page [Int]
     */
    @GET("repos/{owner}/{repo}/commits")
    @Headers("Page:page")
    fun getRepoCommits(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        //SHA or branch to start listing commits from. Default: the repository’s default branch (usually master).
        @Query("page") page: Int,
        @Query("sha") branch: String = "master",
        @Query("per_page") per_page: Int = Api.PAGE_SIZE
    ): LiveData<Page<List<RepoCommit>>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param sha [String]
     */
    @GET("repos/{owner}/{repo}/commits/{sha}")
    fun getCommitInfo(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("sha") sha: String
    ): LiveData<RepoCommitExt>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param page [Int]
     * @param ref [String]
     * @param per_page [Int]
     */
    @GET("repos/{owner}/{repo}/commits/{ref}/comments")
    fun getCommitComments(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Path("ref") ref: String,
        @Query("per_page") per_page: Int = Api.PAGE_SIZE
    ): LiveData<ArrayList<RepoCommit>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param before [String]
     * @param head [String]
     */
    @GET("repos/{owner}/{repo}/compare/{before}...{head}")
    fun compareTwoCommits(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("before") before: String,
        @Path("head") head: String
    ): LiveData<CommitsComparison>
}