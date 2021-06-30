package cn.lgh.openapp.widget

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import java.util.regex.Pattern

/**
 * @author lgh
 * @date 2021/6/29
 *
 */

fun String.highLight(): SpannableString {
    val p = Pattern.compile("<em[^>]*>(.*?)</em>")
    val m = p.matcher(this)
    var str = this
    while (m.find()) {
        val source = m.group()
        val replace = m.group(1) ?: ""
        val start = str.indexOf(source)
        str = str.replace(source, replace)
        val sp = SpannableString(str)
        sp.setSpan(
            ForegroundColorSpan(Color.parseColor("#5A88EA")),
            start,
            start + replace.length,
            0
        )
        return sp
    }
    return SpannableString(this)
}
