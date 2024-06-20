package com.sevenexp.craftit.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.sevenexp.craftit.databinding.FragmentBookmarkBinding
import com.sevenexp.craftit.ui.search.SearchActivity

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
        setupButton()
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchbar)
            searchView.editText.setOnEditorActionListener { _, actionId, _ ->
                searchbar.setText(searchView.text)
                searchView.hide()
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    moveToSearchResult(searchView.text.toString())
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupButton() {
        with(binding) {
            btnImageSearch.setOnClickListener {
                val intent = Intent(requireContext(), SearchActivity::class.java)
                intent.putExtra(SearchActivity.EXTRA_TAKE, true)
                startActivity(intent)
            }
        }
    }

    private fun moveToSearchResult(query: String) {
        val intent = Intent(requireContext(), SearchActivity::class.java)
        intent.putExtra(SearchActivity.EXTRA_QUERY, query)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}