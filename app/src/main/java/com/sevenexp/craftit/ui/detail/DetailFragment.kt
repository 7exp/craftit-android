package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sevenexp.craftit.data.response.DetailResponse
import com.sevenexp.craftit.databinding.FragmentDetailBinding

private const val CRAFT = "craft"

class DetailFragment : Fragment() {
    private val binding by lazy { FragmentDetailBinding.inflate(layoutInflater) }
    private lateinit var craft: DetailResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            craft = it.getParcelable(CRAFT, DetailResponse)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }


    companion object {
        @JvmStatic
        fun newInstance(detail) =
            DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(CRAFT_TITLE, title)
                }
            }
    }
}