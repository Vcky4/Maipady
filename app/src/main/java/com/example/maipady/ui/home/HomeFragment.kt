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


//    private val recyclerView: RecyclerView by bindView(R.id.recycler_view)
//    private val appbar: AppBarLayout by bindView(R.id.appbar)
//    private val drawerIcon: View by bindView(R.id.drawer_icon)
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
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.text.observe(viewLifecycleOwner, Observer {
        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}