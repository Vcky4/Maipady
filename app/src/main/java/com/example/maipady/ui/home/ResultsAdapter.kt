package com.example.maipady.ui.home

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.util.rangeTo
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.R
import com.example.maipady.databinding.ItemListBinding
import com.example.maipady.models.Results
import com.example.maipady.models.TableItems
import java.math.RoundingMode
import java.util.*
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class ResultsAdapter(val activity: FragmentActivity?) :
    RecyclerView.Adapter<ResultsAdapter.RankingViewHolder>() {

    private val result = mutableListOf<Results>()
    

    inner class RankingViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter = TableAdapter()
        fun bindItems(items: Results) {


            binding.semester.text = items.semester
            binding.level.text = items.level + "level"

            var totalCh = 0
            var totalQp = 0
            var cgpa = 0


            items.details.forEach {
                totalCh = totalCh.plus(it.cH.toInt())
                binding.chTotal.text = totalCh.toString()

                when {
                    it.grades.uppercase(Locale.getDefault()) == "A" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(5))
                        binding.qpTotal.text = totalQp.toString()
                    }
                    it.grades.uppercase(Locale.getDefault()) == "B" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(4))
                        binding.qpTotal.text = totalQp.toString()
                    }
                    it.grades.uppercase(Locale.getDefault()) == "C" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(3))
                        binding.qpTotal.text = totalQp.toString()
                    }
                    it.grades.uppercase(Locale.getDefault()) == "D" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(2))
                        binding.qpTotal.text = totalQp.toString()
                    }
                    it.grades.uppercase(Locale.getDefault()) == "E" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(1))
                        binding.qpTotal.text = totalQp.toString()
                    }
                    it.grades.uppercase(Locale.getDefault()) == "F" -> {
                        totalQp = totalQp.plus(it.cH.toInt().times(0))
                        binding.qpTotal.text = totalQp.toString()
                    }
                }
                items.gpa = totalQp.toFloat().div(totalCh.toFloat()).toBigDecimal().setScale(2,RoundingMode.FLOOR).toString()
                binding.gpaText.text = items.gpa

                cgpa = cgpa.plus(items.gpa.toInt())

            }
            cgpaValue(cgpa.toString())
            binding.recy.layoutManager = LinearLayoutManager(activity)
            binding.recy.adapter = adapter
            adapter.setUpRanks(items.details)
        }

        val drawable = binding.chevron
        val gpa = binding.gpaText
        val totalCh = binding.chTotal

        // function to expand view when chevron button is clicked
        fun expand() {
            if (binding.expandView.visibility == GONE) {
                binding.expandView.visibility = VISIBLE
                binding.chevron.setBackgroundResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                binding.expandView.visibility = GONE
                binding.chevron.setBackgroundResource(R.drawable.ic_chevron_right)
            }

        }


    }

    fun setUpRanks(items: List<Results>) {
        this.result.addAll(items)
    }

    fun cgpaValue(value: String) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        return RankingViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = result[position]
        holder.bindItems(item)
        holder.drawable.setOnClickListener {
            holder.expand()
            onItemClickListener?.let { it(item) }
        }


    }

    private var onItemClickListener: ((Results) -> Unit)? = null
    fun setOnItemClickListener(listener: (Results) -> Unit) {
        onItemClickListener = listener
    }


    override fun getItemCount(): Int {
        return result.size
    }

}