package cn.lgh.openapp.http

import cn.lgh.openapp.bean.*
import kotlinx.coroutines.Deferred
import retrofit2.http.*

/**
 * @author lgh
 * @date 2020/9/27
 *
 */
interface ApiService {

    @GET("")
    fun login(): Deferred<String>

    @HEAD("/")
    suspend fun preLoad():Void

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

    @GET(URLConstants.GET_ARTICLE_WX_LIST_URL)
    suspend fun getWXArticleList(): BaseResult<MutableList<WXAuthor>>

    @GET(URLConstants.GET_WX_ARTICLE_BY_ID)
    suspend fun getWXArticleListById(
        @Path("page") page: Int,
        @Path("id") id: Int
    ): BaseResult<WXArticleResult>

    @FormUrlEncoded
    @POST(URLConstants.SEARCH_BY_KEY)
    suspend fun search(
        @FieldMap params: Map<String, String?>,
        @Path("page") page: Int
    ): BaseResult<Article>

    @GET(URLConstants.GET_QA_LIST)
    suspend fun getQAList(@Path("page") page: Int): BaseResult<Article>

    @GET(URLConstants.GET_QA_COMMENT_LIST)
    suspend fun getCommentById(@Path("id") id:Int):BaseResult<Article>
}