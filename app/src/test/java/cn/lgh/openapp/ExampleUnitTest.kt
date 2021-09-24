package cn.lgh.openapp

import cn.lgh.openapp.test.AreaInfo
import com.google.gson.Gson
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }



    @Test
    fun testJson(){
        val json="{\"lists\":[{\"dcode\":\"11\",\"dname\":\"北京\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"12\",\"dname\":\"天津\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"13\",\"dname\":\"河北\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"14\",\"dname\":\"山西\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"15\",\"dname\":\"内蒙古\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"21\",\"dname\":\"辽宁\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"22\",\"dname\":\"吉林\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"23\",\"dname\":\"黑龙江\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"31\",\"dname\":\"上海\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"32\",\"dname\":\"江苏\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"33\",\"dname\":\"浙江\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"34\",\"dname\":\"安徽\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"35\",\"dname\":\"福建\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"36\",\"dname\":\"江西\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"37\",\"dname\":\"山东\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"41\",\"dname\":\"河南\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"42\",\"dname\":\"湖北\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"43\",\"dname\":\"湖南\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"44\",\"dname\":\"广东\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"45\",\"dname\":\"广西\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"46\",\"dname\":\"海南\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"50\",\"dname\":\"重庆\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"51\",\"dname\":\"四川\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"52\",\"dname\":\"贵州\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"53\",\"dname\":\"云南\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"54\",\"dname\":\"西藏\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"61\",\"dname\":\"陕西\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"62\",\"dname\":\"甘肃\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"63\",\"dname\":\"青海\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"64\",\"dname\":\"宁夏\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"65\",\"dname\":\"新疆\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"68\",\"dname\":\"澳门\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"66\",\"dname\":\"香港\",\"pcode\":\"\",\"type\":\"province\"},{\"dcode\":\"67\",\"dname\":\"台湾\",\"pcode\":\"\",\"type\":\"province\"}],\"isSuccess\":true,\"responseCode\":\"0\",\"responseMsg\":\"请求成功\"}"
        val info=Gson().fromJson<AreaInfo>(json,AreaInfo::class.java)
        println("cccc: ${info.responseMsg}")
    }
}