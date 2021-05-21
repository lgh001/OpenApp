package cn.lgh.openapp.widget.webview.cache.loader

import cn.lgh.openapp.widget.webview.cache.offline.WebResource

/**
 * @author lgh
 * @date 2021/5/21
 *
 */
interface ResourceLoader {
    fun getResource(request: SourceRequest?): WebResource?
}