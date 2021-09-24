package cn.lgh.openapp.http.progress

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*

/**
 * @author lgh
 * @date 2021/7/19
 * 上传进度代理
 */
class ProgressRequestBody(
    private val requestBody: RequestBody?,
    private val progressRequestListener: ProgressRequestListener?
) : RequestBody() {

    private var bufferedSink: BufferedSink? = null

    override fun contentType(): MediaType? = requestBody?.contentType()

    override fun contentLength(): Long {
        return requestBody?.contentLength()?:0L
    }

    override fun writeTo(sink: BufferedSink) {
        if (bufferedSink == null) {
            bufferedSink = sink(sink).buffer()
        }
    }

    private fun sink(sink: Sink): Sink {
        return object : ForwardingSink(sink) {

            private var currentWritten = 0L
            private var total = 0L

            override fun write(source: Buffer, byteCount: Long) {
                super.write(source, byteCount)
                if (total == 0L) {
                    //获取内容总长度
                    total = contentLength()
                }
                //当前写入
                currentWritten += byteCount
                progressRequestListener?.onRequestProgress(
                    currentWritten,
                    total,
                    currentWritten == total
                )
            }
        }
    }
}