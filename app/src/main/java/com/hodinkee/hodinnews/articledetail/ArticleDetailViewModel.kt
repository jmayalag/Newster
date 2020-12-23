package com.hodinkee.hodinnews.articledetail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.hodinnews.Article
import com.hodinkee.hodinnews.news.data.ArticleDao
import com.hodinkee.hodinnews.toDto
import com.hodinkee.hodinnews.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ArticleDetailViewModel @ViewModelInject @Inject constructor(
    private val articleDao: ArticleDao
) : ViewModel() {
    val errorEvent = SingleLiveEvent<String>()
    val deleteSuccesEvent = SingleLiveEvent<Unit>()

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