package com.incursio.newster.articledetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incursio.newster.Article
import com.incursio.newster.news.data.ArticleDao
import com.incursio.newster.toDto
import com.incursio.newster.toView
import com.incursio.newster.util.SingleLiveEvent
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ArticleDetailViewModel @ViewModelInject @Inject constructor(
    private val articleDao: ArticleDao
) : ViewModel() {
    val errorEvent = SingleLiveEvent<String>()
    val deleteSuccesEvent = SingleLiveEvent<Unit>()

    fun getArticleFlow(article: Article) = articleDao.findByIdFlow(article.id)
        .filterNotNull()
        .map { it.toView() }


    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            try {
                val count = articleDao.delete(article.toDto())
                Timber.d("Deleted: %s", count)
                deleteSuccesEvent.call()
            } catch (e: Exception) {
                errorEvent.value = e.message
            }
        }
    }
}