package cn.lgh.openapp.ui.base

import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.viewbinding.ViewBinding
import kotlin.reflect.KClass


@MainThread
public inline fun <reified VB : ViewBinding> ComponentActivity.viewBindings(
    noinline inflate: ((LayoutInflater) -> VB)
): Lazy<VB> {
    inflate.invoke(layoutInflater);

    return ViewBindingLazy(inflate,layoutInflater)
}

//@NonNull
//public static ActivityTestBinding inflate(@NonNull LayoutInflater inflater) {
//    return inflate(inflater, null, false);
//}

@MainThread
public inline fun <reified VB : ViewBinding> Fragment.viewBindings(
    noinline inflate: ((LayoutInflater) -> VB)
): Lazy<VB> {
    inflate.invoke(layoutInflater);
    return ViewBindingLazy(inflate,layoutInflater)
}


public class ViewBindingLazy<VB : ViewBinding> (
    private val inflate: (LayoutInflater) -> VB,
    private val layoutInflater: LayoutInflater
) : Lazy<VB> {
    private var cached: VB? = null

    override val value: VB
        get() {
            val viewBinding = cached
            return viewBinding ?: (inflate(layoutInflater).also { cached = it })
        }

    override fun isInitialized(): Boolean = cached != null
}

