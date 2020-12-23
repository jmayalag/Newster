package com.hodinkee.hodinnews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.hodinkee.hodinnews.articledetail.ArticleDetailFragmentArgs
import com.hodinkee.hodinnews.databinding.ArticleFormFragmentBinding
import com.hodinkee.hodinnews.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticleFormFragment : Fragment() {
    private val viewModel: ArticleFormViewModel by viewModels()
    private val args: ArticleFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ArticleFormFragmentBinding.inflate(inflater, container, false)

        args.article?.let { viewModel.editArticle(it) }

        binding.vm = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        viewModel.apply {
            errorEvent.observe(viewLifecycleOwner) {
                context?.let { ctx -> Toast.makeText(ctx, it, Toast.LENGTH_LONG).show() }
            }

            articleCreatedEvent.observe(viewLifecycleOwner) {
                context?.let { ctx ->
                    Toast.makeText(ctx, "Article created", Toast.LENGTH_LONG).show()
                }
                findNavController().popBackStack()
            }
        }

        binding.apply {
            saveButton.setOnClickListener {
                hideKeyboard()
                viewModel.saveArticle()
            }
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}