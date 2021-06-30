package cn.lgh.openapp.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author lgh
 * @date 2021/6/29
 *
 */
@Entity
data class SearchInfo(@PrimaryKey var searchKey: String, var date: Long = System.currentTimeMillis())