package com.example.rumble.presentation.ui.article

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticlesUseCase
import kotlinx.coroutines.launch
import com.example.rumble.domain.common.Result
import com.example.rumble.presentation.ui.login.LoginActivity.Companion.KEY_SUBSCRIPTIONS
import com.example.rumble.presentation.ui.utils.getValue
import kotlinx.coroutines.flow.*

class ArticleViewModel(
    private val context: Context,
    private val articlesUseCase: ArticlesUseCase
) : ViewModel() {

    private val mutableViewState = MutableSharedFlow<ArticleState>()
    val assetsViewState: SharedFlow<ArticleState> = mutableViewState

    fun getArticles(token:String) {
        viewModelScope.launch {
            when (val result = articlesUseCase.invoke(token)) {
                is Result.Success -> {
                    mutableViewState.emit(ArticleState.ArticlesLoaded(result.data))
                }
                is Result.Error -> {
                    mutableViewState.emit(ArticleState.LoadingArticlesFailed("Failed loading article"))
                }
            }
        }
    }

    fun onEvent(event: Event) {
        val data = context.getValue<List<String>>(KEY_SUBSCRIPTIONS)
        viewModelScope.launch {
            when (event) {
                is Event.ShowDetails -> {
                    if(shouldShowDetails(data, event.item.subscriptionType)){
                        mutableViewState.emit(ArticleState.LoadDetails(event.item))
                    } else {
                        mutableViewState.emit(ArticleState.NoDetails)
                    }
                }
            }
        }
    }

    private fun shouldShowDetails(subscriptionType: List<String>?, articleType: String): Boolean {
        if (subscriptionType == null) {
            return false
        }

        return subscriptionType.contains("full") ||
                (articleType.contains("free") || articleType.contains("sports")) &&
                subscriptionType.contains("sports")
    }

    sealed class Event {
        data class ShowDetails(val item: Article) : Event()
    }

    sealed class ArticleState {
        object Loading: ArticleState()
        object NoDetails : ArticleState()
        data class ArticlesLoaded(val items: List<Article>) : ArticleState()
        data class LoadingArticlesFailed(val error: String) : ArticleState()
        data class LoadDetails(val item: Article) : ArticleState()
    }
}