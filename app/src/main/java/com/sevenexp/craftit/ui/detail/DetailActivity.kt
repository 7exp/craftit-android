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
import com.sevenexp.craftit.data.response.HandicraftDetailItem
import com.sevenexp.craftit.data.response.items.StepItem
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.databinding.ActivityDetailBinding
import com.sevenexp.craftit.utils.ResultState
import com.sevenexp.craftit.utils.TopSnackBar
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DetailViewModel>(factoryProducer = { Locator.DetailViewModelFactory })
    private val snackBar by lazy { TopSnackBar(binding.root) }
    private var steps: List<StepItem> = emptyList()
    private var recipe: HandicraftDetailItem? = null
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
            viewModel.detailState.collect { res ->
                when (val result = res.resultGetDetail) {
                    is ResultState.Success -> {
                        if (result.data != null) {
                            totalStep = result.data.data.totalStep
                            recipe = result.data.data
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
                viewModel.insertHistory(getHistoryEntity(0))
                viewModel.updateStep(steps[0], getHistoryEntity(0))
                currentStep = 0
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, StepsFragment.newInstance(totalStep))
                    .addToBackStack(null)
                    .commit()
                setupVisibleButton(BTN_VISIBILITY.STEP)
            }
            btnNex.setOnClickListener {
                currentStep++
                if (currentStep < totalStep) {
                    viewModel.updateStep(steps[currentStep], getHistoryEntity(currentStep))
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FinishFragment.newInstance())
                        .commit()
                    setupVisibleButton(BTN_VISIBILITY.NONE)
                }
            }
            btnBack.setOnClickListener {
                currentStep--
                if (currentStep >= 0) {
                    viewModel.updateStep(steps[currentStep], getHistoryEntity(currentStep))
                } else {
                    supportFragmentManager.popBackStack()
                    setupVisibleButton(BTN_VISIBILITY.DETAIL)
                }
            }
        }

    }

    fun setupVisibleButton(id: BTN_VISIBILITY) {
        when (id) {
            BTN_VISIBILITY.DETAIL -> {
                binding.btnNex.hide()
                binding.btnBack.hide()
                binding.btnStart.show()
            }

            BTN_VISIBILITY.STEP -> {
                binding.btnNex.show()
                binding.btnBack.show()
                binding.btnStart.hide()
            }

            BTN_VISIBILITY.NONE -> {
                binding.btnNex.hide()
                binding.btnBack.hide()
                binding.btnStart.hide()
            }

        }
    }

    enum class BTN_VISIBILITY {
        DETAIL, STEP, NONE
    }

    private fun getHistoryEntity(currentStep: Int) = HistoryEntity(
        totalStep = totalStep,
        id = recipe!!.id,
        image = recipe!!.image,
        name = recipe!!.name,
        currentStep = currentStep,
    )

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

