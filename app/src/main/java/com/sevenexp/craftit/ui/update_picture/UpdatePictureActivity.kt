package com.sevenexp.craftit.ui.update_picture

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityUpdatePictureBinding
import com.sevenexp.craftit.ui.MainActivity
import com.sevenexp.craftit.ui.camera.CameraActivity
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

class UpdatePictureActivity : AppCompatActivity() {
    private val binding by lazy { ActivityUpdatePictureBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UpdateProfilePictureViewModel>(factoryProducer = { Locator.updateProfilePictureFactory })
    private val snackbar by lazy { TopSnackBar(binding.root) }
    private val destinationUri by lazy {
        Uri.fromFile(
            File.createTempFile(
                "cropped-${System.currentTimeMillis()}",
                ".jpg"
            )
        )
    }

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
                val imageUri = Uri.parse(result.data?.getStringExtra(CameraActivity.EXTRA_RESULT))
                cropImage(imageUri)
            }
        }

    private fun cropImage(imageUri: Uri) {

        UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(UCrop.Options().apply {
                setCompressionQuality(100)
                setHideBottomControls(true)
                setToolbarColor(ContextCompat.getColor(this@UpdatePictureActivity, R.color.primary))
                setStatusBarColor(
                    ContextCompat.getColor(
                        this@UpdatePictureActivity,
                        R.color.primary
                    )
                )
                setCircleDimmedLayer(true)
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
            })
            .start(this)
    }

    private fun doSetImage(uri: Uri) {
        binding.ivImage.setImageURI(uri)
        setLoading(false)
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.updateState.collect { state ->
                Log.d("UpdatePictureActivity", "setupListener: ${state.updateResult}")
                when (state.updateResult) {
                    is ResultState.Error -> {
                        snackbar.error(getString(R.string.error_failed_upload_profile_picture))
                        setLoading(false)
                    }

                    is ResultState.Loading -> setLoading(true)
                    is ResultState.Success -> {
                        toMainActivity()
                        setLoading(false)
                    }

                    is ResultState.Idle -> Unit
                }
            }
        }
    }

    private fun setLoading(isLoading: Boolean = false) {
        val color = if (isLoading) R.color.primary100 else R.color.primary
        val visibility = if (isLoading) View.VISIBLE else View.GONE
        val bgColor = ContextCompat.getColor(this@UpdatePictureActivity, color)

        binding.btnUpload.setBackgroundColor(bgColor)
        binding.loading.loadingComponents.visibility = visibility
    }

    private fun setupButton() {
        with(binding) {
            btnSelectImage.setOnClickListener { getImage() }
            btnUpload.setOnClickListener {
                doUpload()
            }
            btnSkip.setOnClickListener { toMainActivity() }
        }
    }

    private fun getImage() {
        resultLauncher.launch(Intent(this, CameraActivity::class.java))
    }

    private fun doUpload() {
        if (destinationUri != null) {
            viewModel.updateImage(destinationUri!!.toFile())
        } else {
            snackbar.error(getString(R.string.please_select_an_image_first))
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val image = UCrop.getOutput(data!!)
            doSetImage(image!!)
        }
    }

    private fun toMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}