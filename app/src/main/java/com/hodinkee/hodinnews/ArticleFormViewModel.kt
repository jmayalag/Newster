package com.hodinkee.hodinnews

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodinkee.hodinnews.news.data.ArticleDao
import com.hodinkee.hodinnews.news.data.ArticleDto
import com.hodinkee.hodinnews.news.data.Category
import com.hodinkee.hodinnews.util.SingleLiveEvent
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
                val article = ArticleDto(
                    UUID.randomUUID().toString(),
                    author = "User",
                    source = "Local",
                    title = title,
                    url = "",
                    urlToImage = null,
                    content = content,
                    publishedAt = Date(),
                    category = Category.LOCAL,
                    description = null
                )

                articleDao.insert(
                    ArticleDto(
                        UUID.randomUUID().toString(),
                        author = "",
                        source = "",
                        title = title,
                        url = "",
                        urlToImage = null,
                        content = content,
                        publishedAt = Date(),
                        category = Category.LOCAL,
                        description = null
                    )
                )
                articleCreatedEvent.value = article.toView()
            } catch (e: Exception) {
                Timber.e(e, "Error creating article")
                errorEvent.value = "Could not create the article"
            }
        }
    }
}