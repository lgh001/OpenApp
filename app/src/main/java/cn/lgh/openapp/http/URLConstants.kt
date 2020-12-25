package cn.lgh.openapp.http

import cn.lgh.openapp.App

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
object URLConstants {

    private const val BASE_URL_DEBUG = "https://www.wanandroid.com/"
    private const val BASE_URL_RELEASE = "https://www.wanandroid.com/"

    val BASE_URL = if (App.DEBUG) BASE_URL_DEBUG else BASE_URL_RELEASE


    //**********************首页************************************

    /**
     * 获取首页作品列表
     */
    const val GET_ARTICLE_LIST_URL="article/list/{page}/json"

    /**
     * 首页banner
     */
    const val GET_HOME_BANNER_URL="banner/json"

    /**
     * 常用网站
     */
    const val GET_FRIEND_URL="friend/json"

    /**
     * 搜索热词
     */
    const val GET_HOT_KEY_URL="hotkey/json"

    /**
     * 置顶文章
     */
    const val GET_TOP_URL="top/json"

    //////////////////////////体系

    /**
     * 体系数据
     * 主要标识的网站内容的体系结构，二级目录。
     */
    const val GET_TREE_URL="tree/json"


    /**
     * 知识体系下的文章
     * cid 分类的id，上述二级目录的id
     * 页码：拼接在链接上，从0开始。
     */
    const val GET_ARTICLE_LIST_BY_ID="article/list/{page}/json"

    /**
     * 获取微信公众号tab
     */
    const val GET_ARTICLE_WX_LIST_URL="wxarticle/chapters/json"

    /**
     * 获取某个公众号的作品列表
     * 参数：
     * 公众号 ID：拼接在 url 中，eg:405
     * 公众号页码：拼接在url 中，eg:1
     */
    const val GET_WX_ARTICLE_BY_ID="list/{id}/{page}/json"

}