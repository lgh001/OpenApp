package cn.lgh.openapp.widget.searchview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import cn.lgh.openapp.bean.SearchInfo
import cn.lgh.openapp.databinding.ItemSearchTagBinding
import cn.lgh.openapp.databinding.ViewSearchBinding
import cn.lgh.openapp.room.AppDatabase
import cn.lgh.openapp.view.FlowLayout

/**
 * @author lgh
 * @date 2021/6/28
 *
 */
class SearchView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private val v: ViewSearchBinding = ViewSearchBinding.inflate(LayoutInflater.from(context))
    private val searchDao by lazy {
        AppDatabase.getInstance(context).searchDao()
    }
    private var adapter: FlowAdapter? = null
    private var history = mutableListOf<SearchInfo?>()

    var onSearch: ((String?) -> Unit)? = null
    var onBack: (() -> Unit)? = null
    var onCancel: ((Boolean) -> Unit)? = null

    private var searching = false

    init {
        addView(
            v.root,
            LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        )
        val h = searchDao.getTopTen()
        h?.let {
            history.addAll(it)
            checkClearHistoryBtn()
        }
        checkClearHistoryBtn()
        adapter = FlowAdapter(context, history)
        adapter?.itemClick = {
            v.etClear.setText(history[it]?.searchKey)
            search(history[it]?.searchKey ?: "", false)
        }
        v.flHistory.setAdapter(adapter)

        v.etClear.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val text = v.etClear.text.toString()
                if (text.isNotEmpty()) {
                    search(text)
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        v.ivBack.setOnClickListener {
            onBack?.invoke()
        }

        v.tvCancel.setOnClickListener {
            if (searching) {
                v.flHistory.visibility = View.VISIBLE
                v.tvClear.visibility = View.VISIBLE
                v.ivClearLog.visibility = View.VISIBLE
            }
            onCancel?.invoke(searching)
            searching = false
        }

        v.ivClearLog.setOnClickListener {
            searchDao.deleteAll()
            history.clear()
            adapter?.notifyDataSetChanged()
        }
    }

    private fun checkClearHistoryBtn() {
        v.tvClear.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
        v.ivClearLog.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun search(text: String, save: Boolean = true) {
        if (text.isEmpty()) {
            return
        }
        searching = true
        v.flHistory.visibility = View.GONE
        v.tvClear.visibility = View.GONE
        v.ivClearLog.visibility = View.GONE
        if (save) searchDao.add(SearchInfo(text))
        onSearch?.invoke(text)
    }

    private class FlowAdapter(val context: Context, val list: List<SearchInfo?>?) :
        FlowLayout.BaseFlowAdapter() {

        var itemClick: ((Int) -> Unit)? = null

        override fun getCount(): Int = list?.size ?: 0

        override fun getView(position: Int): View {
            val v = ItemSearchTagBinding.inflate(LayoutInflater.from(context))
            v.tvName.text = list?.get(position)?.searchKey
            v.tvName.setOnClickListener {
                itemClick?.invoke(position)
            }
            return v.root
        }

    }
}