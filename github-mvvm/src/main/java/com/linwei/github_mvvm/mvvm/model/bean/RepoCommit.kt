package com.linwei.github_mvvm.mvvm.model.bean


import com.google.gson.annotations.SerializedName
import java.io.Serializable

import java.util.ArrayList

open class RepoCommit : Serializable {

    var sha: String? = null
    var url: String? = null
    @SerializedName("html_url")
    var htmlUrl: String? = null
    @SerializedName("comments_url")
    var commentsUrl: String? = null

    var commit: CommitGitInfo? = null
    var author: User? = null
    var committer: User? = null
    var parents: ArrayList<RepoCommit>? = null
}
