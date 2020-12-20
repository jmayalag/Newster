package com.hodinkee.hodinnews.articledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.hodinkee.hodinnews.databinding.ArticleDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private val viewModel: ArticleDetailViewModel by viewModels()
    private val args: ArticleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)
        binding.article = args.article

        return binding.root
    }
}