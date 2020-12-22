package com.hodinkee.hodinnews.articledetail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.hodinkee.hodinnews.databinding.ArticleDetailFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private val viewModel: ArticleDetailViewModel by viewModels()
    private val args: ArticleDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)
        val article = args.article.also {
            binding.article = it
        }

        article.urlToImage?.let {
            binding.image.load(it) {
                this.listener(
                    onStart = { binding.progress.visibility = View.VISIBLE },
                    onSuccess = { _, _ -> binding.progress.visibility = View.GONE },
                    onError = { request, e ->
                        Timber.e(e, "Could not load image. %s", article.urlToImage)
                        binding.progress.visibility = View.GONE
                        binding.image.visibility = View.GONE
                    },
                    onCancel = {
                        binding.progress.visibility = View.GONE
                        binding.image.visibility = View.GONE
                    }
                )
            }
        }

        if (article.url.isNotBlank()) {
            binding.keepReadingBtn.setOnClickListener {
                kotlin.runCatching {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(article.url))
                    context?.startActivity(intent)
                }
            }
        } else {
            binding.keepReadingBtn.visibility = View.GONE
        }

        return binding.root
    }
}