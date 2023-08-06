package com.example.rumble.presentation.ui.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticlesUseCase
import kotlinx.coroutines.launch
import com.example.rumble.domain.common.Result
import kotlinx.coroutines.flow.*

class ArticleViewModel(
    private val articlesUseCase: ArticlesUseCase
) : ViewModel() {

    private val mutableViewState = MutableSharedFlow<ArticleState>(replay = 0)
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
                is Event.ShowDetails -> mutableViewState.emit(ArticleState.LoadDetails(event.item))
               // is Event.NoDetailsDisplay -> mutableViewState.value = ArticleState.NoDetails
            }
        }
    }

    sealed class Event {
        data class ShowDetails(val item: Article) : Event()
    }

    sealed class ArticleState {
        object Loading : ArticleState()
        data class ArticlesLoaded(val items: List<Article>) : ArticleState()
        data class LoadingArticlesFailed(val error: String) : ArticleState()
        data class LoadDetails(val item: Article) : ArticleState()
        object NoDetails : ArticleState()
    }
}