package com.hodinkee.hodinnews.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hodinkee.hodinnews.databinding.RemoteArticleListFragmentBinding
import com.hodinkee.hodinnews.news.ArticlesAdapter
import com.hodinkee.hodinnews.news.ArticlesLoadStateAdapter
import com.hodinkee.hodinnews.toView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
@AndroidEntryPoint
class RemoteArticleListFragment : Fragment() {
    private val viewModel: ArticleListViewModel by viewModels()

    private lateinit var pagingAdapter: ArticlesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = RemoteArticleListFragmentBinding.inflate(inflater, container, false)

        pagingAdapter = ArticlesAdapter(ArticlesAdapter.ArticleComparator) {
            val article = it.toView()
            val directions =
                RemoteArticleListFragmentDirections.toArticleDetailFragment(article)
            findNavController().navigate(directions)
        }
        recyclerView = binding.articlesList
        swipeRefresh = binding.swipeRefresh
        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = ArticlesLoadStateAdapter(pagingAdapter),
            footer = ArticlesLoadStateAdapter(pagingAdapter)
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        swipeRefresh.setOnRefreshListener { pagingAdapter.refresh() }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            pagingAdapter.loadStateFlow.collectLatest { loadStates ->
                swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.remoteArticlesFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        /*viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            pagingAdapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { recyclerView.smoothScrollToPosition(0) }
        }*/
    }
}