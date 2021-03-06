package com.linwei.github_mvvm.mvvm.model.api.service

import androidx.lifecycle.LiveData
import com.linwei.github_mvvm.mvvm.model.api.Api
import com.linwei.github_mvvm.mvvm.model.bean.CommentRequestModel
import com.linwei.github_mvvm.mvvm.model.bean.Issue
import com.linwei.github_mvvm.mvvm.model.bean.IssueEvent
import com.linwei.github_mvvm.mvvm.model.bean.Page
import okhttp3.ResponseBody
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

interface IssueService {
    /**
     * @param owner [String]
     * @param repo [String]
     * @param page [Int]
     * @param state [String]
     * @param sort [String]
     * @param direction [String]
     * @param per_page [Int]
     */
    @GET("repos/{owner}/{repo}/issues")
    @Headers("Page:page","Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun getRepoIssues(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Query("page") page: Int,
        @Query("state") state: String = "all",
        @Query("sort") sort: String = "created",
        @Query("direction") direction: String = "desc",
        @Query("per_page") per_page: Int = Api.PAGE_SIZE
    ): LiveData<Page<List<Issue>>>

    /**
     * @param filter [String]
     * @param state [String]
     * @param sort [Int]
     * @param direction [String]
     * @param page [String]
     */
    @GET("user/issues")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun getUserIssues(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Query("filter") filter: String,
        @Query("state") state: String,
        @Query("sort") sort: String,
        @Query("direction") direction: String,
        @Query("page") page: Int
    ): LiveData<List<Issue>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     */
    @GET("repos/{owner}/{repo}/issues/{issueNumber}")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun getIssueInfo(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int
    ): LiveData<Issue>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     * @param page [String]
     */
    @GET("repos/{owner}/{repo}/issues/{issueNumber}/timeline")
    @Headers("Page:page","Accept: application/vnd.github.mockingbird-preview")
    fun getIssueTimeline(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int,
        @Query("page") page: Int
    ): LiveData<Page<List<IssueEvent>>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     * @param page [Int]
     * @param per_page [Int]
     */
    @GET("repos/{owner}/{repo}/issues/{issueNumber}/comments")
    @Headers("Page:page","Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun getIssueComments(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int,
        @Query("page") page: Int,
        @Query("per_page") per_page: Int = Api.PAGE_SIZE
    ): LiveData<Page<List<IssueEvent>>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     * @param page [Int]
     */
    @GET("repos/{owner}/{repo}/issues/{issueNumber}/events")
    @Headers("Accept: application/vnd.github.html")
    fun getIssueEvents(
        @Header("forceNetWork") forceNetWork: Boolean,
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int,
        @Query("page") page: Int
    ): LiveData<List<IssueEvent>>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     */
    @POST("repos/{owner}/{repo}/issues/{issueNumber}/comments")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun addComment(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int,
        @Body body: CommentRequestModel
    ): LiveData<IssueEvent>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param commentId [String]
     * @param  body [CommentRequestModel]
     */
    @PATCH("repos/{owner}/{repo}/issues/comments/{commentId}")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun editComment(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("commentId") commentId: String,
        @Body body: CommentRequestModel
    ): LiveData<IssueEvent>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param commentId [String]
     */
    @DELETE("repos/{owner}/{repo}/issues/comments/{commentId}")
    fun deleteComment(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("commentId") commentId: String
    ): LiveData<ResponseBody>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     */
    @PATCH("repos/{owner}/{repo}/issues/{issueNumber}")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun editIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int,
        @Body body: Issue
    ): LiveData<Issue>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param body [Issue]
     */
    @POST("repos/{owner}/{repo}/issues")
    @Headers("Accept: application/vnd.github.html,application/vnd.github.VERSION.raw")
    fun createIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body body: Issue
    ): LiveData<Issue>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     */
    @PUT("repos/{owner}/{repo}/issues/{issueNumber}/lock")
    fun lockIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int
    ): LiveData<ResponseBody>

    /**
     * @param owner [String]
     * @param repo [String]
     * @param issueNumber [Int]
     */
    @DELETE("repos/{owner}/{repo}/issues/{issueNumber}/lock")
    fun unLockIssue(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Path("issueNumber") issueNumber: Int
    ): LiveData<ResponseBody>


}