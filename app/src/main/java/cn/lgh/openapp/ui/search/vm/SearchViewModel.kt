package cn.lgh.openapp.ui.search.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.Article
import cn.lgh.openapp.bean.ArticleInfo
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.request
import kotlinx.coroutines.launch

/**
 * @author lgh
 * @date 2021/6/29
 *
 */
class SearchViewModel : BaseViewModel() {

    val searchResult = MutableLiveData<MutableList<ArticleInfo>>()

    fun search(key: String?, page: Int = 0) {

        viewModelScope.launch {
            request {
                val param = hashMapOf<String, String?>()
                param["k"] = key
                repo.search(param, page)
            }?.let {
                println(it.data?.datas)
                searchResult.value = it.data?.datas
            }
        }
    }
}