package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sevenexp.craftit.data.response.items.StepItem
import com.sevenexp.craftit.databinding.FragmentStepsBinding
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.launch

class StepsFragment : Fragment() {
    private val binding by lazy { FragmentStepsBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<DetailViewModel>()
    private val shimmerDrawable = ShimmerDrawable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shimmerDrawable.setShimmer(
            Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(0.9f)
                .setHighlightAlpha(1f)
                .setAutoStart(true)
                .setTilt(35f)
                .build()
        )
        binding.ibBack.setOnClickListener { requireActivity().finish() }
        setupListener()
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.detailState.collect {
                when (val result = it.resultStep) {
                    is ResultState.Success -> it.resultStep.data?.let { it1 -> setupView(it1) }

                    else -> Unit
                }
            }
        }
    }

    private fun setupView(data: StepItem) {
        val totalStep = arguments?.getInt("totalStep")
        with(binding) {
            tvTitle.text = data.name
            tvCurrentStep.text = data.stepNumber.toString()
            tvTotalStep.text = totalStep.toString()
            tvDescription.text = data.description
            Glide.with(requireContext())
                .load(data.image)
                .placeholder(shimmerDrawable)
                .into(ivCraftImage)


            val progress = (data.stepNumber.toFloat() / totalStep!!.toFloat()) * 100
            lpiProgress.progress = progress.toInt()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(totalStep: Int) = StepsFragment().apply {
            arguments = Bundle().apply {
                putInt("totalStep", totalStep)
            }
        }
    }
}