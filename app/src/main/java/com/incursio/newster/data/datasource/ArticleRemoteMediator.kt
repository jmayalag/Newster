package com.incursio.newster.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.incursio.newster.data.local.AppDatabase
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newster.data.local.Category
import com.incursio.newster.data.local.RemoteKeyDto
import com.incursio.newster.data.toDto
import com.incursio.newsapi.NEWS_START_PAGE
import com.incursio.newsapi.NewsService
import retrofit2.HttpException
import timber.log.Timber

@ExperimentalPagingApi
class ArticleRemoteMediator(
    private val db: AppDatabase,
    private val newsService: NewsService
) : RemoteMediator<Int, ArticleDto>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ArticleDto>
    ): MediatorResult {
        val remoteKeyDao = db.remoteKeyDao()
        val articleDao = db.articleDao()

        try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> NEWS_START_PAGE
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKey = remoteKeyDao.remoteKeyByType(Category.REMOTE)

                    if (remoteKey?.nextPageKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }

                    remoteKey.nextPageKey
                }
            }

            val pageSize = state.config.pageSize
            val response = newsService.fetchNews(pageSize, loadKey)
            val nextKey = loadKey + 1
            val articles = response.articles
            val endOfPaginationReached = articles.isEmpty()

            db.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByType(Category.REMOTE.name)
                    articleDao.clearAll(Category.REMOTE)
                }

                remoteKeyDao.insert(RemoteKeyDto(Category.REMOTE, nextKey))
                articleDao.insert(*articles.map { it.toDto() }.toTypedArray())
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: HttpException) {
            return when (e.code()) {
                426 -> MediatorResult.Success(endOfPaginationReached = true)
                else -> MediatorResult.Error(e)
            }
        } catch (e: Exception) {
            Timber.e(e)
            return MediatorResult.Error(e)
        }
    }
}