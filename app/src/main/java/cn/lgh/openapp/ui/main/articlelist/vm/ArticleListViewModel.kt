package cn.lgh.openapp.ui.main.articlelist.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.request
import kotlinx.coroutines.launch

/**
 * @author lgh
 * @date 2020/10/12
 *
 */
class ArticleListViewModel : BaseViewModel() {

    val articleList = MutableLiveData<MutableList<ArticleInfo>>().apply {
        value = mutableListOf()
    }

    var id = 0
    var page = 0

    fun getArticleList() {
        if (id == 0) {
            return
        }
        viewModelScope.launch {
            request (isShowLoading = true){
                repo.getArticleListById(page, id)
            }?.let {
                if (it.data?.datas.isNullOrEmpty()) {
                    noMore(page==0)
                } else {
                    loadSuccess()
                    articleList.value = it.data?.datas
                    page++
                }
            }
        }
    }

}