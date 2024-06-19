package com.sevenexp.craftit.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sevenexp.craftit.databinding.FragmentFinishBinding

private const val CRAFT_ID = "craft_id"
private const val CRAFT_TITLE = "title"
private const val CRAFT_IMAGE = "image"

class FinishFragment : Fragment() {
    private val binding by lazy { FragmentFinishBinding.inflate(layoutInflater) }

    private var storyId: String? = null
    private var craftTitle: String? = null
    private var craftImage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            storyId = it.getString(CRAFT_ID)
            craftTitle = it.getString(CRAFT_TITLE)
            craftImage = it.getString(CRAFT_IMAGE)
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
        fun newInstance(craftId: String, title: String, image: String) =
            FinishFragment().apply {
                arguments = Bundle().apply {
                    putString(CRAFT_ID, craftId)
                    putString(CRAFT_TITLE, title)
                    putString(CRAFT_IMAGE, image)
                }
            }
    }
}