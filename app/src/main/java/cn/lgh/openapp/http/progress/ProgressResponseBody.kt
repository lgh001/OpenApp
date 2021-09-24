package cn.lgh.openapp.http.progress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * @author lgh
 * @date 2021/7/19
 * 下载进度包装类
 *
 * 原理
 * okhttp本身没有提供进度条的监听
 * 而文件上传和下载的原理是将文件编程二进制流，流入流出服务器
 * 所以我们需要找到具体是谁操作流，就能知道每次写入写出流的大小和进度了
 * 而实际上，是MultipartBody执行了这个动作，所以通过静态代理的方式，
 * 增强MultipartBody的写入写出能力，使其支持进度
 */
class ProgressResponseBody(
    private var responseBody: ResponseBody?,
    private val progressRequestListener: ProgressRequestListener?
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentLength(): Long = responseBody?.contentLength() ?: 0L

    override fun contentType(): MediaType? = responseBody?.contentType()

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody?.source())?.buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source?): Source? {
        if (source == null) return null
        return object : ForwardingSource(source) {

            //总长度
            var current = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                var bytesRead = super.read(sink, byteCount)
                //增加当前读取数，如果读取完成返回-1
                current += if (bytesRead != -1L) bytesRead else 0

                progressRequestListener?.onRequestProgress(current, contentLength(), current == -1L)
                return bytesRead
            }
        }
    }
}