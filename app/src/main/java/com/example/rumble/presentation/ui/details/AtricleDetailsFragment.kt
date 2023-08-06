package com.example.rumble.presentation.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.rumble.R
import com.example.rumble.databinding.FragmentDetailsBinding
import com.example.rumble.domain.model.Article
import com.example.rumble.presentation.ui.article.ArticleFragment.Companion.ARTICLE_ID_KEY
import com.example.rumble.presentation.ui.login.LoginActivity.Companion.KEY_TOKEN
import com.example.rumble.presentation.ui.utils.getValue
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment: Fragment() {

    private var rootView : FragmentDetailsBinding? = null
    private val binding get() = rootView!!

    private val viewModel: ArticleDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.articleDetailsViewState.collect(::renderStates)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        rootView = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleId = arguments?.getLong(ARTICLE_ID_KEY)
        val token = requireContext().getValue<String>(KEY_TOKEN)
        if (token != null && articleId != null) {
            viewModel.getDetails(token, articleId)
        } else {
            Log.i("Error in DetailsFragment", " There is no value for articleId or token.")
        }
    }

    private fun renderStates(state: ArticleDetailsViewModel.ArticleDetailsState) {
        when (state) {
            is ArticleDetailsViewModel.ArticleDetailsState.DetailsLoaded -> {
                showDetails(state.item)
            }
            is ArticleDetailsViewModel.ArticleDetailsState.LoadingDetailsFailed -> {
                state.error
            }
        }
    }

    private fun showDetails(item: Article) {
        Glide.with(this)
            .load(item.imageURL)
            .error(R.drawable.ic_launcher_background)
            .into(binding.articleCover)
        binding.authorTitle.showContent(requireContext().getText(R.string.author_title).toString())
        binding.author.showContent(item.authorName)

        binding.title1Title.showContent(requireContext().getText(R.string.title1_title).toString())
        binding.title1.showContent(item.title1)

        binding.title2Title.showContent(requireContext().getText(R.string.title2_title).toString())
        binding.title2.showContent(item.title2)

        binding.keyword1Title.showContent(requireContext().getText(R.string.keyword1_title).toString())
        binding.keyword1.showContent(item.keyword1)

        binding.keyword2Title.showContent(requireContext().getText(R.string.keyword2_title).toString())
        binding.keyword2.showContent(item.keyword2)

        binding.imageCaptionTitle.showContent(requireContext().getText(R.string.image_caption_title).toString())
        binding.imageCaption.showContent(item.imageCaption)

        binding.imageCopyrightTitle.showContent(requireContext().getText(R.string.image_copyright_title).toString())
        binding.imageCopyright.showContent(item.imageCopyright)

        binding.datePublishedTitle.showContent(requireContext().getText(R.string.date_published_title).toString())
        binding.datePublished.showContent(item.datePublished)

        item.contentHTML?.let {
            binding.contentTitle.showContent(requireContext().getText(R.string.content_title).toString())
            binding.contentHTML.loadDataWithBaseURL(
                null,
                item.contentHTML,
                "text/html",
                "UTF-8",
                null
            )
        }?: kotlin.run {  binding.contentHTML.visibility = View.GONE }
    }
}

fun TextView.showContent(value: String?) {
    value?.let {
        text = value
        visibility = View.VISIBLE
    } ?: run {
        visibility = View.GONE
    }
}
