package com.example.rumble.presentation.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rumble.domain.common.Result
import com.example.rumble.domain.model.Article
import com.example.rumble.domain.usecases.ArticleDetailsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class ArticleDetailsViewModel(
    private val articleDetailsUseCase: ArticleDetailsUseCase
) : ViewModel() {

    private val mutableViewState = MutableSharedFlow<ArticleDetailsState>(replay = 0)
    val articleDetailsViewState: SharedFlow<ArticleDetailsState> = mutableViewState

    fun getDetails(token: String, articleId: Long) {
        viewModelScope.launch {
            when (val result = articleDetailsUseCase.invoke(token, articleId)) {
                is Result.Success -> {
                    mutableViewState.emit(ArticleDetailsState.DetailsLoaded(result.data))
                }
                is Result.Error -> {
                    // I could take the exception and format it/map it to some meaningful error message
                    mutableViewState.emit(ArticleDetailsState.LoadingDetailsFailed("Failed loading article details"))
                }
            }
        }
    }

    sealed class ArticleDetailsState {
        data class DetailsLoaded(val item: Article) : ArticleDetailsState()
        data class LoadingDetailsFailed(val error: String) : ArticleDetailsState()
    }
}