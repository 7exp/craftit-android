package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.sevenexp.craftit.Locator
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.response.items.StepItem
import com.sevenexp.craftit.databinding.ActivityDetailBinding
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>(factoryProducer = { Locator.DetailViewModelFactory })
    private val snackBar by lazy { TopSnackBar(binding.root) }
    private var steps: List<StepItem> = emptyList()
    private var totalStep = 0
    private var currentStep = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val craftId = intent.getStringExtra(EXTRA_CRAFT_ID)
        val title = intent.getStringExtra(EXTRA_TITLE)

        if (craftId.isNullOrEmpty() || title.isNullOrEmpty()) {
            Log.e("DetailActivity", "Invalid craft id, or title")
            Log.e("DetailActivity", "craftId: $craftId, title: $title")
            finish()
            return
        }

        viewModel.getDetail(craftId)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, DetailFragment.newInstance()).commit()

        setupButton()
        setupListener()
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.detailState.collect {
                when (val result = it.resultGetDetail) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            totalStep = result.data.data.totalStep
                            steps = result.data.data.detailHandicraft.sortedBy { it.stepNumber }
                        } else {
                            snackBar.error(getString(R.string.data_not_found))
                        }
                    }

                    is ResultState.Error -> snackBar.error(getString(R.string.data_not_found))

                    else -> Unit
                }
            }
        }
    }

    private fun setupButton() {
        with(binding) {
            btnStart.setOnClickListener {
                viewModel.updateStep(steps[0], totalStep)
                currentStep = 0
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StepsFragment.newInstance(totalStep))
                    .addToBackStack(null)
                    .commit()
                btnStart.hide()
                btnBack.show()
                btnNex.show()
                btnToDetail.hide()
            }
            btnNex.setOnClickListener {
                currentStep++
                if (currentStep < totalStep) {
                    viewModel.updateStep(steps[currentStep], totalStep)
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FinishFragment.newInstance())
                        .commit()
                    btnToDetail.show()
                    btnStart.hide()
                    btnBack.hide()
                    btnNex.hide()
                }
            }
            btnBack.setOnClickListener {
                currentStep--
                if (currentStep >= 0) {
                    viewModel.updateStep(steps[currentStep], totalStep)
                } else {
                    supportFragmentManager.popBackStack()
                    btnStart.show()
                    btnBack.hide()
                    btnNex.hide()
                }
            }
        }

    }

    companion object {
        const val EXTRA_CRAFT_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
    }

    private fun Button.hide() {
        this.visibility = View.GONE
    }

    private fun Button.show() {
        this.visibility = View.VISIBLE
    }
}

