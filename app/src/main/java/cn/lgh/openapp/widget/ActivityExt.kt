package cn.lgh.openapp.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.Fragment
import cn.lgh.openapp.widget.IntentFieldMethod.internalMap
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * @author lgh
 * @date 2020/9/28
 * activity intent fragment 相关的扩展函数
 */


fun Activity.toast(msg: String?) {
    //兼容小米手机toast前面带应用名问题
    val toast=Toast.makeText(this,null,Toast.LENGTH_SHORT)
    toast.setText(msg)
    toast.show()
}

fun Context.toast(msg: String?) {
    //兼容小米手机toast前面带应用名问题
    val toast=Toast.makeText(this,null,Toast.LENGTH_SHORT)
    toast.setText(msg)
    toast.show()
}

fun Fragment.toast(msg: String?) = this.activity?.run {
//    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    toast(msg)
}

/**
 * [Intent]的扩展方法，此方法可无视类型直接获取到对应值
 * 如getStringExtra()、getIntExtra()、getSerializableExtra()等方法通通不用
 * 可以直接通过此方法来获取到对应的值，例如：
 * <pre>
 *     var mData: List<String>? = null
 *     mData = intent.get("Data")
 * </pre>
 * 而不用显式强制转型
 *
 * @param key 对应的Key
 * @return 对应的Value
 */
fun <O> Intent?.get(key: String, defaultValue: O? = null) =
    this?.internalMap()?.get(key) as? O ?: defaultValue

/**
 * 作用同Intent.[get]
 */
fun <O> Bundle?.get(key: String, defaultValue: O? = null) =
    this?.internalMap()?.get(key) as? O ?: defaultValue

fun <T> Intent.putExtras(vararg params: Pair<String, T>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Serializable -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
        }
    }
    return this
}

/**
 * 作用同[Activity.startActivity]
 * <pre>
 *      //不携带参数
 *      startActivity<TestActivity>()
 *
 *      //携带参数（可连续多个键值对）
 *      startActivity<TestActivity>("Key" to "Value")
 *  </pre>
 */
inline fun <reified TARGET : Activity> Activity.startActivity(
    vararg params: Pair<String, Any?>
) {
    startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

inline fun <reified TARGET : Activity> Fragment.startActivity(
    vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

/**
 *  Activity跳转，同[Activity.startActivity]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivity(this, TestActivity::class)
 *
 *      //携带参数（可连续多个键值对）
 *     startActivity(
 *         TestActivity::class,
 *         "Key1" to "Value",
 *         "Key2" to 123
 *     )
 *  </pre>
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 */
fun Activity.startActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>
) = startActivity(Intent(this, target.java).putExtras(*params))

fun Fragment.startActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target.java).putExtras(*params))
}

/**
 * 作用同上，以下三个方法为了兼容Java Class
 */
fun Activity.startActivity(
    target: Class<out Activity>, vararg params: Pair<String, Any?>
) = startActivity(Intent(this, target).putExtras(*params))

fun Fragment.startActivity(
    target: Class<out Activity>, vararg params: Pair<String, Any?>
) = activity?.run {
    startActivity(Intent(this, target).putExtras(*params))
}

//inline fun <reified TARGET:Activity> Activity.startActivityForResult(
//    vararg params:Pair<String,Any?>,crossinline callback:((result:Intent?)->Unit)
//)=startActivityForResult(TARGET::class,*params,callback = callback)

inline fun <reified TARGET : Activity> Activity.startActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) = startActivityForResult(TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> Fragment.startActivityForResult(
    vararg params: Pair<String, Any?>, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.startActivityForResult(TARGET::class, *params, callback = callback)

/**
 *  作用同[Activity.startActivityForResult]
 *  示例：
 *  <pre>
 *      //不携带参数
 *      startActivityForResult(this, TestActivity::class) {
 *          if (it == null) {
 *              //未成功处理，即（ResultCode != RESULT_OK）
 *          } else {
 *              //处理成功，这里可以操作返回的intent
 *          }
 *      }
 *  </pre>
 *  携带参数同[startActivity]
 *
 * @param target 要启动的Activity
 * @param params extras键值对
 * @param callback onActivityResult的回调
 */
inline fun Activity.startActivityForResult(
    target: KClass<out Activity>,vararg params:Pair<String,Any?>,
    crossinline callback: (Intent?) -> Unit
)=ActivityMessenger.startActivityForResult(this,target, *params, callback=callback)

inline fun Fragment.startActivityForResult(
    target: KClass<out Activity>, vararg params:Pair<String,Any?>,
    crossinline callback: (result: Intent?) -> Unit
) = activity?.run {
    ActivityMessenger.startActivityForResult(this,target,*params,callback = callback)
}

/**
 * 作用同上，以下方法为了兼容Java Class
 */
inline fun Activity.startActivityForResult(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)

inline fun Fragment.startActivityForResult(
    target: Class<out Activity>, vararg params: Pair<String, Any?>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run {
    ActivityMessenger.startActivityForResult(this, target, *params, callback = callback)
}

internal object IntentFieldMethod {
    private val bundleClass =
        (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) BaseBundle::class else Bundle::class).java

    private val mExtras: Field? by lazy {
        Intent::class.java.getDeclaredField("mExtras").also { it.isAccessible = true }
    }

    private val mMap: Field? by lazy {
        runSafely {
            bundleClass.getDeclaredField("mMap").also {
                it.isAccessible = true
            }
        }
    }

    private val unparcel: Method? by lazy {
        runSafely {
            bundleClass.getDeclaredMethod("unparcel").also {
                it.isAccessible = true
            }
        }
    }

    internal fun Intent.internalMap() = runSafely {
        mMap?.get((mExtras?.get(this) as? Bundle).also {
            it?.run { unparcel?.invoke(this) }
        }) as? Map<String, Any?>
    }

    internal fun Bundle.internalMap() = runSafely {
        unparcel?.invoke(it)
        mMap?.get(it) as? Map<String, Any?>
    }
}





