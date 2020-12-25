package cn.lgh.openapp.ui.main.common.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.WXArticleInfo
import cn.lgh.openapp.bean.WXAuthor
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.request
import kotlinx.coroutines.launch

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class CommonViewModel : BaseViewModel() {

    val authorList = MutableLiveData<MutableList<WXAuthor>>()

    val articleList = MutableLiveData<List<WXArticleInfo>>()

    var page = 1

    /**
     * 获取公众号列表
     */
    fun getAuthorList() {
        viewModelScope.launch {
            request (isShowLoading = true,block={
                repo.getWXArticleList()
            })?.let {
                authorList.value = it.data
            }
        }
    }

    /**
     * 根据用户id获取作品
     * @param id Int id
     */
    fun getArticlesById(id: Int = 0) {
        viewModelScope.launch {
            request {
                repo.getWXArticleListById(page, id)
            }?.let {
                page++
                articleList.value = it.data?.datas ?: listOf()
            }
        }
    }

    /**
     * 刷新作品列表
     * @param id Int
     */
    fun refreshArticles(id: Int = 0) {
        viewModelScope.launch {
            request {
                repo.getWXArticleListById(1, id)
            }?.let {
                page=1
                articleList.value = it.data?.datas ?: listOf()
            }
        }
    }
}