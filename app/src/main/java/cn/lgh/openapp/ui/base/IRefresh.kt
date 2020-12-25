package cn.lgh.openapp.ui.base

/**
 * @author lgh
 * @date 2020/12/17
 *
 */
interface IRefresh {

    fun hasRefresh(): Boolean

    fun onRefresh()

    fun onLoadMore()
}