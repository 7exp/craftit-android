package com.sevenexp.craftit.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.databinding.FragmentDetailBinding
import com.sevenexp.craftit.utils.ResultState
import kotlinx.coroutines.launch
import java.net.URLEncoder

private const val CRAFT = "craft"

class DetailFragment : Fragment() {
    private val binding by lazy { FragmentDetailBinding.inflate(layoutInflater) }
    private val viewModel: DetailViewModel by activityViewModels()
    private val shimmerDrawable = ShimmerDrawable()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        return binding.root
    }

    private fun setupListener() {
        lifecycleScope.launch {
            viewModel.detailState.collect {
                when (val result = it.resultGetDetail) {
                    is ResultState.Success -> {
                        if (result.data != null)
                            setupView(result.data)
                        else
                            binding.tvTitle.text = "Data not found"
                    }

                    is ResultState.Error -> {
                        binding.tvTitle.text = result.message
                    }

                    else -> Unit
                }

            }
        }
    }

    private fun setupView(data: DetailResponse) {
        val craft = data.data
        with(binding) {
            tvTitle.text = craft.name
            tvDescription.text = craft.description
            tvCategory.text = craft.tags.joinToString(", ")
            tvUsername.text = craft.createdBy
            btnStep.text = craft.totalStep.toString()
            btnLike.text = craft.likes.toString()
            source.setOnClickListener {
                val link = ContextCompat.getString(
                    requireContext(),
                    R.string.instructable_search_string
                ) + URLEncoder.encode(craft.name, "UTF-8") + "&projects=all"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(intent)
            }
            Glide.with(requireContext())
                .load(craft.image)
                .placeholder(shimmerDrawable)
                .into(ivCraftImage)
            Glide.with(requireContext())
                .load(craft.imageUser)
                .placeholder(shimmerDrawable)
                .into(ivUserProfilePhoto)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DetailFragment()
    }

}

