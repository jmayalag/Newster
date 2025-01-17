package com.incursio.newster.view.form

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.incursio.newster.data.local.ArticleDao
import com.incursio.newster.data.local.ArticleDto
import com.incursio.newster.data.local.Category
import com.incursio.newster.data.toDto
import com.incursio.newster.data.toView
import com.incursio.newster.util.SingleLiveEvent
import com.incursio.newster.view.Article
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ArticleFormViewModel @ViewModelInject @Inject constructor(
    private val articleDao: ArticleDao
) : ViewModel() {
    val title = MutableLiveData("")
    val content = MutableLiveData("")

    val titleError = MutableLiveData("")
    val contentError = MutableLiveData("")

    val errorEvent = SingleLiveEvent<String>()
    val articleCreatedEvent = SingleLiveEvent<Article>()

    private var savedArticle: Article? = null
    private var imageUri: String? = null

    fun editArticle(article: Article) {
        title.value = article.title
        content.value = article.content
        savedArticle = article
    }

    fun saveArticle() {
        Timber.d("%s, %s", title.value, content.value)
        val title = title.value ?: ""
        val content = content.value ?: ""
        var hasErrors = false

        if (title.isBlank()) {
            titleError.value = "Invalid title"
            hasErrors = true
        } else {
            titleError.value = ""
        }

        if (content.isBlank()) {
            contentError.value = "Invalid content"
            hasErrors = true
        } else {
            contentError.value = ""
        }

        if (hasErrors) {
            return
        }

        viewModelScope.launch {
            try {
                val article = upsert(title, content)
                articleCreatedEvent.value = article.toView()
            } catch (e: Exception) {
                Timber.e(e, "Error creating article")
                errorEvent.value = "Could not create the article"
            }
        }
    }

    private suspend fun upsert(title: String, content: String): ArticleDto {
        savedArticle?.toDto()?.let {
            val updated = it.copy(title = title, content = content, urlToImage = imageUri)
            articleDao.update(updated)
            return updated
        }

        val article = ArticleDto(
            UUID.randomUUID().toString(),
            author = "User",
            source = "Local",
            title = title,
            url = "",
            urlToImage = imageUri,
            content = content,
            publishedAt = Date(),
            category = Category.LOCAL,
            description = null
        )

        articleDao.insert(article)
        return article
    }

    fun setImage(photoUri: String) {
        imageUri = photoUri
    }
}