package com.linwei.github_mvvm.mvvm.model.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.linwei.github_mvvm.mvvm.model.bean.EventPayload
import com.linwei.github_mvvm.mvvm.model.bean.Repository
import com.linwei.github_mvvm.mvvm.model.bean.User
import java.util.*

/**
 * ---------------------------------------------------------------------
 * @Author: WeiShuai
 * @Time: 2020/9/30
 * @Contact: linwei9605@gmail.com"d
 * @Follow: https://github.com/WeiShuaiDev
 * @Description: 收到事件表
 *-----------------------------------------------------------------------
 */
@Entity(tableName = "received_event")
data class ReceivedEventEntity(
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "data")
    @SerializedName("data")
    var data: String? = null
)