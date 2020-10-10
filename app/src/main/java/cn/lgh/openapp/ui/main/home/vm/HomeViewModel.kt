package cn.lgh.openapp.ui.main.home.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.Article
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.bean.BannerInfo
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.request
import kotlinx.coroutines.launch

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
class HomeViewModel : BaseViewModel() {

    val banner =
        MutableLiveData<MutableList<BannerInfo>>().apply {
            value = mutableListOf()
        }

    val articleListData =
        MutableLiveData<MutableList<ArticleInfo>>().apply {
            value = mutableListOf()
        }

    var page=0

    fun getBannerData() {
        viewModelScope.launch {
            request {
                repo.getBannerList()
            }?.let {
                banner.value = it.data
            }
        }
    }

    fun refreshList(){
        page=0
        getArticleList()
    }

    fun getArticleList() {
        viewModelScope.launch {
            request {
                repo.getArticleList(page)
            }?.let {
                articleListData.value = it.data?.datas
                page++
            }
        }
    }
}