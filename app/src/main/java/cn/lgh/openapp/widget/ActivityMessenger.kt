package cn.lgh.openapp.widget

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * @author lgh
 * @date 2020/9/29
 *
 */
object ActivityMessenger {

    private var sRequestCode = 0
        set(value) {
            field = if (value >= Int.MAX_VALUE) 1 else value
        }

    /**
     *  作用同[Activity.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity<TestActivity>(this)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.startActivity<TestActivity>(this, "Key" to "Value")
     *  </pre>
     *
     * @param TARGET 要启动的Activity
     * @param starter 发起的Activity
     * @param params extras键值对
     */
    inline fun <reified TARGET : Activity> startActivity(
        starter: Activity, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    /**
     * Adapter跳转，同[Context.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity<TestActivity>(context)
     *
     *      //携带参数（可连续多个键值对）
     *      ActivityMessenger.startActivity<TestActivity>(context, "Key" to "Value")
     *  </pre>
     *
     * @param TARGET 要启动的Context
     * @param starter 发起的Fragment
     * @param params extras键值对
     */
    inline fun <reified TARGET : Activity> startActivity(
        starter: Context, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    /**
     *  作用同[Activity.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity(this, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.startActivity(
     *         this, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     *  </pre>
     *
     * @param starter 发起的Activity
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: Activity, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Activity, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target).putExtras(*params))

    /**
     *  Fragment跳转，同[Activity.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity(this, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.startActivity(
     *         this, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     *  </pre>
     *
     * @param starter 发起的Fragment
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: Fragment, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter.activity, target.java).putExtras(*params))

    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Fragment, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter.activity, target).putExtras(*params))

    /**
     *  Adapter里面跳转，同[Context.startActivity]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivity(context, TestActivity::class)
     *
     *      //携带参数（可连续多个键值对）
     *     ActivityMessenger.startActivity(
     *         context, TestActivity::class,
     *         "Key1" to "Value",
     *         "Key2" to 123
     *     )
     *  </pre>
     *
     * @param starter 发起的Context
     * @param target 要启动的Activity
     * @param params extras键值对
     */
    fun startActivity(
        starter: Context, target: KClass<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))


    /**
     *  作用同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {
     *          if (it == null) {
     *              //未成功处理，即（ResultCode != RESULT_OK）
     *          } else {
     *              //处理成功，这里可以操作返回的intent
     *          }
     *      }
     *  </pre>
     *  携带参数同[startActivity]
     *
     * @param TARGET 要启动的Activity
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun <reified TARGET : Activity> startActivityForResult(
        starter: Activity, vararg params: Pair<String, Any?>,
        crossinline callback: (Intent?) -> Unit
    ) = startActivityForResult(starter, TARGET::class, *params, callback = callback)

    /**
     *  Fragment跳转，同[Activity.startActivityForResult]
     *  示例：
     *  <pre>
     *      //不携带参数
     *      ActivityMessenger.startActivityForResult<TestActivity> {
     *          if (it == null) {
     *              //未成功处理，即（ResultCode != RESULT_OK）
     *          } else {
     *              //处理成功，这里可以操作返回的intent
     *          }
     *      }
     *  </pre>
     *  携带参数同[startActivity]
     *
     * @param TARGET 要启动的Activity
     * @param starter 发起的Activity
     * @param params extras键值对
     * @param callback onActivityResult的回调
     */
    inline fun <reified TARGET : Activity> startActivityForResult(
        starter: Fragment, vararg params: Pair<String, Any?>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = startActivityForResult(starter.activity, TARGET::class, *params, callback = callback)


    /**
     * 作用同上，此方法为了兼容Java Class
     */
    fun startActivity(
        starter: Context, target: Class<out Activity>, vararg params: Pair<String, Any?>
    ) = starter.startActivity(Intent(starter, target).putExtras(*params))


    inline fun startActivityForResult(
        starter: Activity?, target: KClass<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: (Intent?) -> Unit
    ) = starter.runIfNonNull {
        startActivityForResult(it, Intent(it, target.java).putExtras(*params), callback)
    }

    inline fun startActivityForResult(
        starter: Activity?, target: Class<out Activity>,
        vararg params: Pair<String, Any?>, crossinline callback: (Intent?) -> Unit
    ) = starter.runIfNonNull {
        startActivityForResult(it, Intent(it, target).putExtras(*params), callback)
    }

    @Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")
    inline fun startActivityForResult(
        starter: Activity?, intent: Intent, crossinline callback: (Intent?) -> Unit
    ) = starter.runIfNonNull {
        if (it is FragmentActivity) {
            val fragment = GhostFragment()
            fragment.init(++this.sRequestCode, intent) { result ->
                callback(result)
                it.supportFragmentManager.beginTransaction().remove(fragment)
                    .commitAllowingStateLoss()
            }
        } else {
            throw Throwable("Activity must be FragmentActivity")
        }
    }

    /**
     *  作用同[Activity.finish]
     *  示例：
     *  <pre>
     *      ActivityMessenger.finish(this, "Key" to "Value")
     *  </pre>
     *
     * @param src 发起的Activity
     * @param params extras键值对
     */
    fun finish(src: Activity, vararg params: Pair<String, Any?>) = src.run {
        setResult(Activity.RESULT_OK, Intent().putExtras(*params))
        finish()
    }

    fun finish(src: Fragment, vararg params: Pair<String, Any?>) = src.activity?.run {
        finish(this, *params)
    }
}

class GhostFragment : Fragment() {

    private var requestCode = -1
    private var intent: Intent? = null
    private var callback: ((Intent?) -> Unit)? = null

    fun init(requestCode: Int, intent: Intent, callback: (Intent?) -> Unit) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intent?.let {
            startActivityForResult(it, requestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            var result = if (resultCode == Activity.RESULT_OK && data != null) data else null
            callback?.let { it(result) }
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
    }
}


class ActivityExtras<T>(
    private val extraName: String,
    val defaultValue: T
) : ReadWriteProperty<Activity, T> {

    /**
     * getExtras字段对应值
     */
    private var extra: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T {
        //如果extra不为空则返回extra
        //如果extra是空，则判断intent的参数的值，如果值不为空，则将值赋给intent，并返回
        //如果intent参数的值也为空，这返回defaultValue
        return extra ?: thisRef.intent?.get<T>(extraName)?.also { extra = it }
        ?: defaultValue.also {
            extra = it
        }
    }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        extra = value
    }

}

/**
 * 获取Intent参数，Fragment
 * 示例同[ActivityExtras]
 */
class FragmentExtras<T>(private val extraName: String, private val defaultValue: T) :
    ReadWriteProperty<Fragment, T> {

    /**
     * getExtras字段对应的值
     */
    private var extra: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        // 如果extra不为空则返回extra
        // 如果extra是空的，则判断intent的参数的值，如果值不为空，则将值赋予extra，并且返回
        // 如果intent参数的值也为空，则返回defaultValue，并且将值赋予extra
        return extra ?: thisRef.arguments?.get<T>(extraName)?.also { extra = it }
        ?: defaultValue.also { extra = it }
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        extra = value
    }
}

fun <T> extraFrag(extraName: String): FragmentExtras<T?> = FragmentExtras(extraName, null)

fun <T> extraFrag(extraName: String, defaultValue: T): FragmentExtras<T?> =
    FragmentExtras(extraName, defaultValue)

fun <T> extraAct(extraName: String): ActivityExtras<T?> = ActivityExtras(extraName, null)

fun <T> extraAct(extraName: String, defaultValue: T): ActivityExtras<T> =
    ActivityExtras(extraName, defaultValue)