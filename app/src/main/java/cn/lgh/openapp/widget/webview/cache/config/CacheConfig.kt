package cn.lgh.openapp.widget.webview.cache.config

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
        private var cacheDir: String? = null
        private var diskCacheSize: Long = 0
        private var memCacheSize = 0
        private var mimeTypeFilter: MimeTypeFilter? = null
        private var version: Int = 0

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