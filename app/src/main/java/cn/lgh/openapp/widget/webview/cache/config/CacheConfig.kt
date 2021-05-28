package cn.lgh.openapp.widget.webview.cache.config

import android.content.Context
import cn.lgh.openapp.widget.webview.cache.utils.MemorySizeCalculator
import java.io.File

/**
 * @author lgh
 * @date 2021/5/20
 *
 */
class CacheConfig {

    var cacheDir: String? = null
    var diskCacheSize: Long = 0
    var memCacheSize = 0
    var mimeTypeFilter: MimeTypeFilter? = null
    var version: Int = 0

    class Builder {
        private val CACHE_DIR_NAME = "cached_webview_force"
        private val DEFAULT_DISK_CACHE_SIZE = 100 * 1024 * 1024L
        private var cacheDir: String? = null
        private var diskCacheSize: Long = DEFAULT_DISK_CACHE_SIZE
        private var memCacheSize: Int = MemorySizeCalculator.getSize()
        private var mimeTypeFilter: MimeTypeFilter? = DefaultMimeTypeFilter()
        private var version: Int = 0

        constructor(context: Context) {
            cacheDir = "${context.cacheDir}${File.separator}$CACHE_DIR_NAME"
        }

        fun setCacheDir(dir: String): Builder {
            this.cacheDir = dir
            return this
        }

        fun setVersion(version: Int): Builder? {
            this.version = version
            return this
        }

        fun setDiskCacheSize(diskCacheSize: Long): Builder? {
            this.diskCacheSize = diskCacheSize
            return this
        }

        fun setExtensionFilter(filter: MimeTypeFilter): Builder? {
            mimeTypeFilter = filter
            return this
        }

        fun setMemoryCacheSize(memoryCacheSize: Int): Builder? {
            memCacheSize = memoryCacheSize
            return this
        }

        fun build(): CacheConfig {
            val config = CacheConfig()
            config.cacheDir = this.cacheDir
            config.memCacheSize = this.memCacheSize
            config.diskCacheSize = this.diskCacheSize
            config.version = this.version
            config.mimeTypeFilter = this.mimeTypeFilter
            return config
        }
    }
}