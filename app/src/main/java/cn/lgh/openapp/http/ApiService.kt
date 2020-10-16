package cn.lgh.openapp.http

import cn.lgh.openapp.bean.*
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
interface ApiService {

    @GET("")
    fun login(): Deferred<String>

    @GET(URLConstants.GET_ARTICLE_LIST_URL)
    suspend fun getArticleList(@Path("page") page: Int): BaseResult<Article>

    @GET(URLConstants.GET_HOME_BANNER_URL)
    suspend fun getBannerList(): BaseResult<MutableList<BannerInfo>>

    @GET(URLConstants.GET_TOP_URL)
    suspend fun getTopArticleList(): BaseResult<MutableList<ArticleInfo>>

    @GET(URLConstants.GET_TREE_URL)
    suspend fun getKnowledgeTree(): BaseResult<MutableList<Leaf>>

    @GET(URLConstants.GET_ARTICLE_LIST_BY_ID)
    suspend fun getArticleListById(
        @Path("page") page: Int,
        @Query("cid") id: Int
    ): BaseResult<Article>
}