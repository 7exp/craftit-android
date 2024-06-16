package com.sevenexp.craftit.ui.image_search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityImageSearchBinding
import com.sevenexp.craftit.ui.search_result.SearchResultActivity
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio

class ImageSearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivityImageSearchBinding.inflate(layoutInflater) }
    private val pixOptions = Options().apply {
        ratio = Ratio.RATIO_AUTO
        count = 1
        spanCount = 4
        path = "/craftit/images"
        isFrontFacing = false
        mode = Mode.Picture
        flash = Flash.Auto
        preSelectedUrls = ArrayList<Uri>()
    }


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            Log.d(
                "ImageSearchActivity",
                "requestPermissionLauncher: ${permissions.entries.toString()}"
            )
            if (permissions.entries.all { it.value }) {
                setupPix()
            } else {
                showDismissDialog()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        requestPermissions()
    }

    private fun requestPermissions() {
        var requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

        // Add media permissions for Android 13
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions = arrayOf(
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
            )
        }

        Log.d("ImageSearchActivity", "requestPermissions: $requiredPermissions")
        if (requiredPermissions.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            Log.d("ImageSearchActivity", "requestPermissions: granted")
            setupPix()
        } else {
            Log.d("ImageSearchActivity", "requestPermissions: not granted")
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    private fun setupPix() {
        addPixToActivity(R.id.main, pixOptions) {
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    Log.d("ImageSearchActivity", "setupPix: ${it.data.toString()}")
                    toSearch(it.data)
                }

                PixEventCallback.Status.BACK_PRESSED -> {
                    finish()
                }
            }
        }
    }

    private fun showDismissDialog() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.permission_denied)
            .setMessage(R.string.permission_denied_message)
            .setPositiveButton(R.string.grant_permission) { dialog, _ ->
                dialog.dismiss()
                requestPermissions()
            }
            .setNegativeButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun toSearch(data: List<Uri>) {
        val intent = Intent(this, SearchResultActivity::class.java)
        intent.putExtra(SearchResultActivity.EXTRA_IMAGE, data[0].toString())
        startActivity(intent)
        finish()
    }


}