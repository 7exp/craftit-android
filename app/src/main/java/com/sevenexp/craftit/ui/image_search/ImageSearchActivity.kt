package com.sevenexp.craftit.ui.image_search

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.sevenexp.craftit.databinding.ActivityImageSearchBinding
import com.sevenexp.craftit.ui.camera.CameraActivity
import com.sevenexp.craftit.ui.search_result.SearchResultActivity

class ImageSearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivityImageSearchBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        getImage()
    }

    private fun getImage() {
        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == CameraActivity.RESULT_CODE && result.data != null) {
                    val image = result.data?.getStringExtra(CameraActivity.EXTRA_RESULT)
                    if (image != null) cropImage(image)
                } else {
                    finish()
                }
            }
        resultLauncher.launch(Intent(this, CameraActivity::class.java))
    }

    private fun cropImage(image: String) {
        val cropImage = registerForActivityResult(CropImageContract()) { result ->
            var uriContent: String = image
            if (result.isSuccessful) {
                uriContent = result.uriContent.toString()
            }
            toSearch(uriContent)
        }

        cropImage.launch(
            CropImageContractOptions(
                uri = image.toUri(),
                cropImageOptions = CropImageOptions(
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true
                )
            )
        )
    }

    private fun toSearch(data: String) {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra(SearchResultActivity.EXTRA_IMAGE, data)
        startActivity(intent)
        finish()
    }


}