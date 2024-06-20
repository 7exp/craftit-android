package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sevenexp.craftit.databinding.FragmentFinishBinding

class FinishFragment : Fragment() {
    private val binding by lazy { FragmentFinishBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = FinishFragment()
    }
}