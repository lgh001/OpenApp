package cn.lgh.openapp.test

import com.google.gson.annotations.SerializedName
import kotlin.math.abs

/**
 * @author lgh
 * @date 2021/7/12
 *
 */
 class AreaInfo{
    val isSuccess: Boolean?=null
    val responseCode: String?=null
    val responseMsg: String?=null
    @SerializedName(value = "lists",alternate = ["data"])
    val lists: List<Lists>?=null
 }

data class Lists(
    val dcode: String,
    val dname: String,
    val pcode: String,
    val type: String
)

data class Poin(val x:Double,val y:Double)


fun test(){
    val p1=Poin(0.0,0.0)
    val p2=Poin(1.0,2.0)
    val p3=Poin(2.0,3.0)
    //1,2
    val loc12=location(p1,p2,p3)
    //2,3
    val loc23=location(p2,p3,p1)
}


fun location(p1:Poin,p2:Poin,p3:Poin):Boolean{
    val slop12=getSlop(p1,p2)
    var useX12=false
    //如果斜率大于1,说明更直线更偏向y，需要使用x来判断左右
    useX12 = slop12 > 1
    var res:Boolean
    if (useX12){
        res=rightToX(p1, p2, p3)
    }else{
        res=topToY(p1, p2, p3)
    }
    var negate12=p1.x>p2.x

    return if (negate12) !res else res
}

fun getSlop(p1:Poin,p2:Poin):Double{
    //y=kx+b
    //p1.y=k*p1.x+b
    //p2.y=k*p2.x+b
    return abs((p1.y-p2.y)/(p1.x-p2.x))
}

fun rightToX(p1:Poin,p2:Poin,p3:Poin):Boolean{
    val k=(p1.y-p2.y)/(p1.x-p2.x)
    val b=p1.y-k*p1.x
    //y=k*x+b
    val x3=(p3.y-b)/k
    return p3.x>x3
}

fun topToY(p1:Poin,p2:Poin,p3:Poin):Boolean{
    val k=(p1.y-p2.y)/(p1.x-p2.x)
    val b=p1.y-k*p1.x
    //y=k*x+b
    val y3=k*p3.x+b
    return p3.y>y3
}