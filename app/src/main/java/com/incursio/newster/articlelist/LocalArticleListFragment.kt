package com.incursio.newster.articlelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.incursio.newster.databinding.LocalArticleListFragmentBinding
import com.incursio.newster.news.ArticlesAdapter
import com.incursio.newster.news.ArticlesLoadStateAdapter
import com.incursio.newster.toView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@ExperimentalPagingApi
@AndroidEntryPoint
class LocalArticleListFragment : Fragment() {
    private val viewModel: ArticleListViewModel by activityViewModels()

    private lateinit var pagingAdapter: ArticlesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = LocalArticleListFragmentBinding.inflate(inflater, container, false)

        pagingAdapter = ArticlesAdapter(ArticlesAdapter.ArticleComparator) {
            val article = it.toView()
            val directions = LocalArticleListFragmentDirections.toArticleDetailFragment(article)
            findNavController().navigate(directions)
        }
        recyclerView = binding.articlesList
        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = ArticlesLoadStateAdapter(pagingAdapter),
            footer = ArticlesLoadStateAdapter(pagingAdapter)
        )

        binding.fab.setOnClickListener {
            findNavController().navigate(
                LocalArticleListFragmentDirections.actionCreateArticle()
            )
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.localArticlesFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
}