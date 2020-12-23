package com.incursio.newster.view.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.incursio.newster.R
import com.incursio.newster.databinding.ArticleDetailFragmentBinding
import com.incursio.newster.data.local.Category
import com.incursio.newster.view.Article
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ArticleDetailFragment : Fragment() {
    private val viewModel: ArticleDetailViewModel by viewModels()
    private val args: ArticleDetailFragmentArgs by navArgs()

    private val isLocal get() = args.article.category == Category.LOCAL
    private lateinit var binding: ArticleDetailFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(isLocal)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ArticleDetailFragmentBinding.inflate(inflater, container, false)

        if (!isLocal) {
            binding.keepReadingBtn.setOnClickListener {
                kotlin.runCatching {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(args.article.url))
                    context?.startActivity(intent)
                }
            }
        } else {
            binding.keepReadingBtn.visibility = View.GONE
        }


        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getArticleFlow(args.article).collectLatest {
                bindArticle(it)
            }
        }

        viewModel.deleteSuccesEvent.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { error ->
            context?.let { Toast.makeText(it, error, Toast.LENGTH_LONG).show() }
        }

        return binding.root
    }

    private fun bindArticle(article: Article) {
        binding.article = article
        article.urlToImage?.let {
            binding.image.load(it) {
                this.listener(
                    onStart = { binding.progress.visibility = View.VISIBLE },
                    onSuccess = { _, _ -> binding.progress.visibility = View.GONE },
                    onError = { _, e ->
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun promptDelete() {
        val activity = activity ?: return
        val builder: AlertDialog.Builder = activity.let {
            AlertDialog.Builder(it)
        }
        builder.apply {
            setMessage(R.string.prompt_delete_article)
            setTitle(R.string.are_you_sure)
            setPositiveButton(R.string.cancel) { _, _ ->

            }
            setNegativeButton(R.string.delete) { _, _ ->
                viewModel.deleteArticle((args.article))
            }
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                promptDelete()
                true
            }
            R.id.action_edit -> {
                findNavController().navigate(
                    ArticleDetailFragmentDirections.actionEditArticle(args.article)
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}