package cn.lgh.openapp.room

import androidx.room.*
import cn.lgh.openapp.bean.SearchInfo

/**
 * @author lgh
 * @date 2021/6/29
 *
 */
@Dao
interface SearchDao {

    @get:Query("SELECT * from SearchInfo")
    val all: List<SearchInfo?>?

    @Query("select * from SearchInfo order by date desc limit 10")
    fun getTopTen(): List<SearchInfo?>?

    @Update
    fun update(search: SearchInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(search: SearchInfo)

    @Query("update SearchInfo set date=:time where searchKey = :key")
    fun update(key: String, time: Long)

    @Delete
    fun delete(info: SearchInfo)

    @Query("delete from SearchInfo")
    fun deleteAll()
}