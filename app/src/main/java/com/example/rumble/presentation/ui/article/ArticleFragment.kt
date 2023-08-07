package com.example.rumble.presentation.ui.article

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.rumble.R
import com.example.rumble.databinding.FragmentArticleBinding
import com.example.rumble.domain.model.Article
import com.example.rumble.presentation.ui.adapter.ArticleAdapter
import com.example.rumble.presentation.ui.login.LoginActivity.Companion.KEY_TOKEN
import com.example.rumble.presentation.ui.utils.getValue
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleFragment : Fragment() {

    private val rootView: FragmentArticleBinding by lazy {
        FragmentArticleBinding.inflate(layoutInflater)
    }

    private val viewModel: ArticleViewModel by viewModel()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return rootView.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        articleAdapter =
            ArticleAdapter(requireContext(), object : ArticleAdapter.ActionClickListener {
                override fun showDetails(item: Article) {
                    viewModel.onEvent(ArticleViewModel.Event.ShowDetails(item))
                }
            })

        rootView.recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 1)
            adapter = articleAdapter
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.assetsViewState.collect(::renderStates)
            }
        }

        val token = requireActivity().getValue<String>(KEY_TOKEN)
        token?.let {

            viewModel.getArticles(token)
        }?: Log.i("ArticleFragment","Token is null.")

    }

    private fun renderStates(state: ArticleViewModel.ArticleState) {
            when (state) {
                is ArticleViewModel.ArticleState.Loading -> {
                    rootView.indicator.visibility = View.VISIBLE
                }
                is ArticleViewModel.ArticleState.ArticlesLoaded -> {
                    rootView.indicator.visibility = View.INVISIBLE
                    articleAdapter.submitUpdate(state.items)
                }
                is ArticleViewModel.ArticleState.LoadingArticlesFailed -> {
                    rootView.indicator.visibility = View.INVISIBLE
                    Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
                }
                is ArticleViewModel.ArticleState.LoadDetails -> {
                    rootView.indicator.visibility = View.INVISIBLE
                    loadDetailFragment(R.id.assetDetailsFragment, state.item.id)
                }
                is ArticleViewModel.ArticleState.NoDetails -> {
                    Toast.makeText(requireContext(), R.string.subscriptions_error, Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun getNavController(): NavController {
        return requireView().findNavController()
    }

    private fun loadDetailFragment(@IdRes viewId: Int, articleId: Long) {
        val bundle = Bundle()
        bundle.putLong(ARTICLE_ID_KEY, articleId)
        getNavController().navigate(viewId, bundle)
    }

    companion object {
        const val ARTICLE_ID_KEY = "articleId"
    }
}