package com.sevenexp.craftit.ui.search

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.ActivitySearchBinding
import com.sevenexp.craftit.ui.adapter.SearchAdapter
import com.sevenexp.craftit.ui.camera.CameraActivity
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.compressImage
import com.sevenexp.craftit.widget.CustomRecyclerView.ViewStatus
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import java.io.File

class SearchActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySearchBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SearchViewModel>(factoryProducer = { Locator.searchResulViewModelFactory })
    private val adapter by lazy { SearchAdapter() }
    private val destinationUri by lazy {
        Uri.fromFile(
            File.createTempFile(
                "cropped-${System.currentTimeMillis()}",
                ".jpg"
            )
        )
    }
    private var query: String? = null
    private var image: File? = null
    private var lastSearch: LastSearch? = null

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == CameraActivity.RESULT_CODE && result.data != null) {
                val imageUri = Uri.parse(result.data?.getStringExtra(CameraActivity.EXTRA_RESULT))
                cropImage(imageUri)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val extraQuery = intent.getStringExtra(EXTRA_QUERY)
        val extraTake = intent.getBooleanExtra(EXTRA_TAKE, false)
        if (!extraQuery.isNullOrEmpty()) {
            lastSearch = LastSearch.QUERY
            query = extraQuery
            binding.etSearch.setText(extraQuery)
            doSearch(query = extraQuery)
        } else if (extraTake) {
            startCamera()
        } else{
            binding.rvSearchResult.showView(ViewStatus.EMPTY)
        }

        setupRecyclerView()
        setupObserver()
        setupButton()
    }

    private fun setupButton() {
        with(binding) {
            btnBack.setOnClickListener { finish() }
            btnCamera.setOnClickListener { startCamera() }
            etSearch.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doSearch(query = etSearch.text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        resultLauncher.launch(intent)
    }

    private fun setupRecyclerView() {
        binding.rvSearchResult.recyclerView.adapter = adapter
        binding.rvSearchResult.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResult.setOnRetryClickListener {
            if (lastSearch == LastSearch.QUERY) doSearch(query = query) else doSearch(image = image)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            viewModel.searchState.collect { result ->
                when (result.searchResult) {
                    is ResultState.Success -> {
                        val searchResult = result.searchResult.data
                        if (!searchResult.isNullOrEmpty()) {
                            binding.rvSearchResult.showView(ViewStatus.ON_DATA)
                            adapter.setData(searchResult)
                        } else {
                            binding.rvSearchResult.showView(ViewStatus.EMPTY)
                        }
                    }

                    is ResultState.Loading -> binding.rvSearchResult.showView(ViewStatus.LOADING)
                    is ResultState.Error -> binding.rvSearchResult.showView(ViewStatus.ERROR)
                    else -> Unit
                }

            }
        }
    }


    private fun cropImage(imageUri: Uri) {
        UCrop.of(imageUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withOptions(UCrop.Options().apply {
                setCompressionQuality(100)
                setHideBottomControls(true)
                setToolbarColor(ContextCompat.getColor(this@SearchActivity, R.color.primary))
                setStatusBarColor(
                    ContextCompat.getColor(
                        this@SearchActivity,
                        R.color.primary
                    )
                )
                setCircleDimmedLayer(true)
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
            })
            .start(this)
    }

    private fun doSearch(query: String? = null, image: File? = null) {
        viewModel.search(query, image)
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            image = UCrop.getOutput(data!!)?.toFile()
            lastSearch = LastSearch.IMAGE
            doSearch(null, image!!.compressImage())
        }
    }

    companion object {
        const val EXTRA_TAKE = "extra_take"
        const val EXTRA_QUERY = "extra_query"

        private enum class LastSearch { IMAGE, QUERY }
    }
}