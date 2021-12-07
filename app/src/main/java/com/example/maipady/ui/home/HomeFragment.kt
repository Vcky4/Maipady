package com.example.maipady.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.marginEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.MainActivity
import com.example.maipady.R
import com.example.maipady.databinding.FragmentHomeBinding
import com.example.maipady.models.DummyData
import com.google.android.material.appbar.AppBarLayout


class HomeFragment : Fragment() {



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

        drawerIcon.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        val resultAdapter = ResultsAdapter(activity)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = resultAdapter

        resultAdapter.setUpRanks(DummyData.listR)

        binding.fabAdd.setOnClickListener {
            if (binding.fabUpload.visibility == GONE){
                binding.fabUpload.visibility = VISIBLE
                binding.fabCreate.visibility = VISIBLE
                binding.fabAdd.setImageResource(R.drawable.ic_baseline_close_24)
            }else{
                binding.fabCreate.visibility = GONE
                binding.fabUpload.visibility = GONE
                binding.fabAdd.setImageResource(R.drawable.ic_sharp_add_24)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}