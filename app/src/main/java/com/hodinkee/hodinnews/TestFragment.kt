package com.hodinkee.hodinnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.hodinkee.hodinnews.databinding.TestFragmentBinding
import com.hodinkee.hodinnews.news.ArticlesAdapter
import com.hodinkee.hodinnews.news.ArticlesLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class TestFragment : Fragment() {
    private val viewModel: TestViewModel by viewModels()

    private lateinit var pagingAdapter: ArticlesAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = TestFragmentBinding.inflate(inflater, container, false).apply {
            vm = viewModel
            lifecycleOwner = this@TestFragment.viewLifecycleOwner
        }

        pagingAdapter = ArticlesAdapter(ArticlesAdapter.ArticleComparator)
        recyclerView = binding.articlesList
        recyclerView.adapter = pagingAdapter.withLoadStateHeaderAndFooter(
            header = ArticlesLoadStateAdapter(pagingAdapter),
            footer = ArticlesLoadStateAdapter(pagingAdapter)
        )

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.articlesFlow.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }
    }
}