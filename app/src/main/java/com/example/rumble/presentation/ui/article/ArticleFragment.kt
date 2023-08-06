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
import com.example.rumble.presentation.ui.login.LoginActivity
import com.example.rumble.presentation.ui.login.LoginActivity.Companion.KEY_TOKEN
import com.example.rumble.presentation.ui.utils.getValue
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleFragment : Fragment() {

    private var rootView : FragmentArticleBinding? = null
    private val binding get() = rootView!!

    private val viewModel: ArticleViewModel by viewModel()
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        rootView = FragmentArticleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        articleAdapter =
            ArticleAdapter(requireContext(), object : ArticleAdapter.ActionClickListener {
                override fun showDetails(item: Article) {
                    binding.indicator.visibility = View.INVISIBLE

                    val subscriptions = requireContext().getValue<List<String>>(LoginActivity.KEY_SUBSCRIPTIONS)
                    subscriptions?.let {
                        if (shouldShowDetails(subscriptions,item.subscriptionType)) {
                            //loadDetailFragment(R.id.assetDetailsFragment, item.id)
                            viewModel.onEvent(ArticleViewModel.Event.ShowDetails(item))
                        } else {
                             Toast.makeText(requireContext(), R.string.subscriptions_error, Toast.LENGTH_SHORT).show()
                            //viewModel.onEvent((ArticleViewModel.Event.NoDetailsDisplay))
                        }
                    }
                }
            })

        binding.recyclerView.apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

    private fun renderStates(state: ArticleViewModel.ArticleState) {
        when (state) {
            is ArticleViewModel.ArticleState.Loading -> {
                binding.indicator.visibility = View.VISIBLE
            }
            is ArticleViewModel.ArticleState.ArticlesLoaded -> {
                binding.indicator.visibility = View.INVISIBLE
                articleAdapter.submitUpdate(state.items)
            }
            is ArticleViewModel.ArticleState.LoadingArticlesFailed -> {
                binding.indicator.visibility = View.INVISIBLE
                Toast.makeText(requireContext(), state.error, Toast.LENGTH_SHORT).show()
            }
            is ArticleViewModel.ArticleState.NoDetails -> {
                Toast.makeText(requireContext(), R.string.subscriptions_error, Toast.LENGTH_SHORT).show()
            }
            is ArticleViewModel.ArticleState.LoadDetails -> {
                loadDetailFragment(R.id.assetDetailsFragment, state.item.id)
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

    private fun shouldShowDetails(subscriptionType:List<String>,articleType: String): Boolean {
        if (subscriptionType.contains("full")) {
            return true
        }

        val isFreeArticle = articleType.contains("free")
        val isSportsArticle = articleType.contains("sports")
        val hasAccess = isFreeArticle || isSportsArticle

        val isSportsSubscription = subscriptionType.contains("sports")

        return isSportsSubscription && hasAccess
    }

    companion object {
        const val ARTICLE_ID_KEY = "articleId"
    }
}