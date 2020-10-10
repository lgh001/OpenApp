package cn.lgh.openapp.event

import org.greenrobot.eventbus.EventBus

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
object Event {

    fun get():EventBus= EventBus.getDefault()
}