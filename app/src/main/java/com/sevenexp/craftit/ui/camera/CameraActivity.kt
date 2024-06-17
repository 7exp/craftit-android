package com.sevenexp.craftit.ui.camera

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivityCameraBinding
import io.ak1.pix.helpers.PixEventCallback
import io.ak1.pix.helpers.addPixToActivity
import io.ak1.pix.models.Flash
import io.ak1.pix.models.Mode
import io.ak1.pix.models.Options
import io.ak1.pix.models.Ratio

class CameraActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
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

        if (requiredPermissions.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            setupPix()
        } else {
            requestPermissionLauncher.launch(requiredPermissions)
        }
    }

    private fun setupPix() {
        addPixToActivity(R.id.main, pixOptions) {
            when (it.status) {
                PixEventCallback.Status.SUCCESS -> {
                    val data = it.data[0]
                    val intent = Intent()
                    intent.putExtra(EXTRA_RESULT, data.toString())
                    setResult(RESULT_CODE, intent)
                    finish()
                }

                PixEventCallback.Status.BACK_PRESSED -> {
                    val intent = Intent()
                    setResult(RESULT_CANCELED, intent)
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

//    result

    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 100
    }
}