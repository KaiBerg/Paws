package com.example.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentPhotoGalleryBinding

class PhotoGalleryFragment : Fragment(R.layout.fragment_photo_gallery){
    private var _binding : FragmentPhotoGalleryBinding? = null
    private val binding
    get() = _binding!!

    private lateinit var adapter: Photo2Adapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = Photo2Adapter(DataManager.photos2.value!!)
        adapter.onItemClick = { photo, position ->
            val action =
                PhotoGalleryFragmentDirections.actionPhotoGalleryFragmentToPhotoView(position)
            findNavController().navigate(action)
        }
        binding.photoDisplay.adapter = adapter
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.photoDisplay.layoutManager = layoutManager


        binding.addPhotoButton.setOnClickListener {
            val action = PhotoGalleryFragmentDirections.actionPhotoGalleryFragmentToPhotoCapture()
            findNavController().navigate(action)
        }

        DataManager.photos2.observe(viewLifecycleOwner) {
            adapter.photos = it
            adapter.notifyDataSetChanged()
        }

        /*binding.searchView.setupWithSearchBar(binding.searchBar)*/

    }

    fun calculateNoOfColumns(
        context: Context,
        columnWidthDp: Float
    ): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}