package cn.lgh.openapp.widget.viewpager

import android.os.Bundle
import androidx.fragment.app.Fragment
import cn.lgh.openapp.ui.main.home.HomeFragment
import kotlin.reflect.KClass

/**
 * @author lgh
 * @date 2020/9/30
 *
 */
data class FragmentInfo(
    val fragment: KClass<out Fragment>,
    val data: Bundle? = null
)