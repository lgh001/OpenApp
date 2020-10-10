package cn.lgh.openapp.widget

import android.view.View
import cn.lgh.openapp.widget.ViewClickDelay.SPACE_TIME
import cn.lgh.openapp.widget.ViewClickDelay.hash
import cn.lgh.openapp.widget.ViewClickDelay.lastClickTime

/**
 * @author lgh
 * @date 2020/9/27
 *
 */

object ViewClickDelay {
    var hash: Int = 0
    var lastClickTime: Long = 0
    var SPACE_TIME: Long = 1000
}

//防止两次点击
infix fun View.clicks(action: () -> Unit) {
    this.setOnClickListener {
        if (this.hashCode() != hash) {
            hash = this.hashCode()
            lastClickTime = System.currentTimeMillis()
            action()
        } else {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime > SPACE_TIME) {
                lastClickTime = System.currentTimeMillis()
                action()
            }
        }
    }
}