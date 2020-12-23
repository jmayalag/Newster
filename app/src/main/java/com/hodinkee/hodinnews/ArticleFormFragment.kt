package com.hodinkee.hodinnews

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.hodinkee.hodinnews.databinding.ArticleFormFragmentBinding
import com.hodinkee.hodinnews.util.hideKeyboard
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.CompositePermissionListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

const val PICK_PHOTO_CODE = 42

@AndroidEntryPoint
class ArticleFormFragment : Fragment() {
    private var binding: ArticleFormFragmentBinding? = null
    private val viewModel: ArticleFormViewModel by viewModels()
    private val args: ArticleFormFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = ArticleFormFragmentBinding.inflate(inflater, container, false).also {
            this.binding = it
        }

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

            addImageButton.setOnClickListener {
                hideKeyboard()
                checkPermissions()
            }
        }

        return binding.root
    }

    fun checkPermissions() {
        val ctx = context ?: return
        val dialogPermissionListener = DialogOnDeniedPermissionListener.Builder
            .withContext(context)
            .withTitle("External Storage")
            .withMessage(getString(R.string.storage_permission_message))
            .withButtonText(android.R.string.ok)
            .build()

        val onSuccessPermissionListener = object : BasePermissionListener() {
            override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                Timber.d("Permission granted %s", p0)
                selectImage()
            }
        }

        val permissionListener = CompositePermissionListener(
            dialogPermissionListener,
            onSuccessPermissionListener
        )

        Dexter.withContext(ctx)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(permissionListener)
            .check()
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        kotlin.runCatching { startActivityForResult(intent, PICK_PHOTO_CODE) }
    }

    private fun loadImage(uri: Uri) {
        val ctx = context ?: return
        binding?.imageView?.load(uri) {
            listener(
                onStart = { r ->
                    Timber.d("Request: %s", r)
                },
                onSuccess = { _, _ ->
                    binding?.imageView?.visibility = View.VISIBLE
                    viewModel.setImage(uri)
                },
                onError = { _, e ->
                    Timber.e(e, "Could not load image")
                    Toast.makeText(ctx, "Could not open your image", Toast.LENGTH_LONG)
                        .show()
                }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            PICK_PHOTO_CODE -> {
                val photoUri = data?.data
                if (photoUri != null) {
                    Timber.d("Selected photo %s", photoUri)
                    loadImage(photoUri)
                }
            }
        }
    }
}