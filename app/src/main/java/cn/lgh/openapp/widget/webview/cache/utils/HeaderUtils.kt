package cn.lgh.openapp.widget.webview.cache.utils

import okhttp3.Headers


/**
 * @author lgh
 * @date 2021/5/21
 *
 */
object HeaderUtils {

    fun generateHeadersMap(headers: Headers): Map<String, String> {
        val map = HashMap<String, String>()
        for (key in headers.names()) {
            val values = StringBuilder()
            for (value in listToSet(headers.values(key))) {
                if (!values.isNullOrEmpty()) {
                    values.append(" ")
                }
                values.append(value)
            }
            map[key] = values.toString().trim()
        }
        return map
    }

    private fun listToSet(origin: List<String>): Set<String> {
        val target = mutableSetOf<String>()
        target.addAll(origin)
        return target
    }

    fun generateHeadersMap(map: Map<String, List<String>?>):Map<String, String> {
        val headersMap: MutableMap<String, String> = HashMap()
        var index = 0
        for (key in map.keys) {
            val values = StringBuilder()
            for (value in map[key] ?: error("")) {
                values.append(value)
                if (index++ > 0) {
                    values.append(",")
                }
            }
            index = 0
            headersMap[key] = values.toString()
        }
        return headersMap
    }
}