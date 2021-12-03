package com.example.maipady.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.R
import com.example.maipady.databinding.FragmentHomeBinding
import com.google.android.material.appbar.AppBarLayout

var animationPlaybackSpeed: Double = 0.8

class HomeFragment : Fragment() {



//    private val filtersLayout: FiltersLayout by bindView(R.id.filters_layout)
//    private val filtersMotionLayout: FiltersMotionLayout by bindView(R.id.filters_motion_layout)


    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        val appbar = binding.appbar
        val drawerIcon = binding.drawerIcon

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}