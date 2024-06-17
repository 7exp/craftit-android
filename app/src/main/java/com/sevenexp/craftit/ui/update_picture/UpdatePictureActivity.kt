package com.sevenexp.craftit.ui.update_picture

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityUpdatePictureBinding
import com.sevenexp.craftit.ui.auth.login.LoginActivity
import com.sevenexp.craftit.ui.camera.CameraActivity
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import com.sevenexp.craftit.utils.compressImage
import com.sevenexp.craftit.utils.toFile
import kotlinx.coroutines.launch
import java.io.File

class UpdatePictureActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdatePictureBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UpdateProfilePictureViewModel>(factoryProducer = { Locator.updateProfilePictureFactory })
    private val snackbar by lazy { TopSnackBar(binding.root) }
    private var uploadImage: File? = null
    private var image: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        setupButton()
        setupListener()
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CameraActivity.RESULT_CODE && result.data != null) {
                image = result.data?.getStringExtra(CameraActivity.EXTRA_RESULT)
                image?.let { cropImage(it) }
            }
        }

    private val cropImageResultLauncher = registerForActivityResult(CropImageContract()) { result ->
        image?.let {
            val localImage = it
            var uriContent: String = localImage
            if (result.isSuccessful) {
                uriContent = result.uriContent.toString()
            }
            uploadImage = uriContent.toUri().toFile(this).compressImage()
            doSetImage()
        }
    }

    private fun doSetImage() {
        binding.ivImage.setImageURI(uploadImage?.toUri())
        setLoading(false)
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.updateState.collect { state ->
                when (state.updateResult) {
                    is ResultState.Error -> {
                        snackbar.error(getString(R.string.error_failed_upload_profile_picture))
                        setLoading()
                    }

                    is ResultState.Loading -> setLoading(true)
                    is ResultState.Success -> {
                        toLogin()
                        setLoading()
                    }

                    is ResultState.Idle -> Unit
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean = false) {
        with(binding) {
            val color = if (isLoading) R.color.primary100 else R.color.primary
            val visibility = if (isLoading) View.VISIBLE else View.GONE
            val bgColor = ContextCompat.getColor(this@UpdatePictureActivity, color)

            btnUpload.setBackgroundColor(bgColor)
            binding.loadingComponents.loadingComponents.visibility = visibility
        }
    }

    private fun setupButton() {
        with(binding) {
            btnSelectImage.setOnClickListener { getImage() }
            btnUpload.setOnClickListener {
                doUpload()
            }
            btnSkip.setOnClickListener { toLogin() }
        }
    }

    private fun getImage() {
        resultLauncher.launch(Intent(this, CameraActivity::class.java))
    }

    private fun cropImage(image: String) {
        cropImageResultLauncher.launch(
            CropImageContractOptions(
                uri = image.toUri(), cropImageOptions = CropImageOptions(
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true,
                    autoZoomEnabled = true,
                    allowCounterRotation = true,
                    multiTouchEnabled = true,
                )
            )
        )
    }

    private fun doUpload() {
        if (uploadImage != null) {
            viewModel.updateImage(uploadImage!!)
        } else {
            snackbar.error(getString(R.string.please_select_an_image_first))
        }
    }

    private fun toLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}