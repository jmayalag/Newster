package com.incursio.newster

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenStarted
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.incursio.newster.databinding.ArticleFormFragmentBinding
import com.incursio.newster.util.hideKeyboard
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.*

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

        args.article?.let {
            viewModel.editArticle(it)
            it.urlToImage?.let { url ->
                binding.imageView.load(url) {
                    listener { _, _ -> binding.imageView.visibility = View.VISIBLE }
                }
            }
        }

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
        val dialogPermissionListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
            .withContext(context)
            .withTitle(R.string.external_storage_permission_title)
            .withMessage(R.string.storage_permission_message)
            .withButtonText(android.R.string.ok)
            .build()

        val onSuccessPermissionListener = object : BaseMultiplePermissionsListener() {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if (report?.areAllPermissionsGranted() == true) {
                    selectImage()
                }
            }
        }

        val permissionsListener = CompositeMultiplePermissionsListener(
            dialogPermissionListener,
            onSuccessPermissionListener
        )

        Dexter.withContext(ctx)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(permissionsListener)
            .check()
    }

    private fun selectImage() {
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
        }.let { Intent.createChooser(it, "Select a Picture") }

        kotlin.runCatching { startActivityForResult(intent, PICK_PHOTO_CODE) }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private fun loadImage(uri: Uri) {
        val ctx = context ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            whenStarted {
                val file = File(
                    ctx.filesDir.toString(),
                    Date().time.toString() + ".jpeg"
                ).also { it.createNewFile() }

                Timber.d("Saving")

                ctx.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    FileOutputStream(file).use { outputStream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                    }

                    binding?.imageView?.load(bitmap) {
                        listener(
                            onStart = { r ->
                                Timber.d("Request: %s", r)
                            },
                            onSuccess = { _, _ ->
                                binding?.imageView?.visibility = View.VISIBLE
                                viewModel.setImage("file://$file")
                            },
                            onError = { _, e ->
                                Timber.e(e, "Could not load image")
                                Toast.makeText(ctx, "Could not open your image", Toast.LENGTH_LONG)
                                    .show()
                            }
                        )
                    }
                }
                Timber.d("Saved to %s", file)
            }
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