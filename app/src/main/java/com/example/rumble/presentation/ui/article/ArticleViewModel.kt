package com.example.rumble.presentation.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticlesUseCase
import kotlinx.coroutines.launch
import com.example.rumble.domain.common.Result
import com.example.rumble.presentation.ui.utils.Subscription
import kotlinx.coroutines.flow.*

class ArticleViewModel(
    private val accountType: Subscription,
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
        viewModelScope.launch {
            when (event) {
                is Event.ShowDetails -> {
                    if(shouldShowDetails(event.item.subscriptionType)){
                        mutableViewState.emit(ArticleState.LoadDetails(event.item))
                    } else {
                        mutableViewState.emit(ArticleState.NoDetails)
                    }
                }
            }
        }
    }

    private fun shouldShowDetails(articleType: String): Boolean {
        return accountType.isUserPremiere() ||
                (articleType.contains("free") || articleType.contains("sports")) &&
                !accountType.isUserPremiere()
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