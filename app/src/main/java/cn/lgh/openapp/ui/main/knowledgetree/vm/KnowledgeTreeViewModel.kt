package cn.lgh.openapp.ui.main.knowledgetree.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.lgh.openapp.bean.Leaf
import cn.lgh.openapp.ui.base.BaseViewModel
import cn.lgh.openapp.widget.request
import kotlinx.coroutines.launch

/**
 * @author lgh
 * @date 2020/10/10
 *
 */
class KnowledgeTreeViewModel : BaseViewModel() {

    val knowledgeTree = MutableLiveData<MutableList<Leaf>>()


    fun getTree() {
        viewModelScope.launch {
            request {
                repo.getKnowledgeTree()
            }?.let {
                it.data.let { data ->
                    knowledgeTree.value = data
                }
            }
        }
    }
}