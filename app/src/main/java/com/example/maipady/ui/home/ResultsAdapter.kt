package com.example.maipady.ui.home

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.R
import com.example.maipady.databinding.ItemListBinding
import com.example.maipady.models.DummyData
import com.example.maipady.models.Results
import com.example.maipady.models.TableItems
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.util.stream.IntStream.range

class ResultsAdapter(val activity: FragmentActivity?) :
    RecyclerView.Adapter<ResultsAdapter.RankingViewHolder>() {

    private val result = mutableListOf<Results>()

    inner class RankingViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val adapter = TableAdapter()
        fun bindItems(items: Results) {


            binding.semester.text = items.semester
            binding.level.text = items.level + "level"
            binding.gpaText.text = items.gpa

            var totalCh = 0
            var totalQp = 0


            items.details.forEach {
                totalCh = totalCh.plus(it.cH.toInt())
                binding.chTotal.text = totalCh.toString()

                if (it.grades == "A"){
                    binding.qp.text = it.cH.toInt().times(5).toString()
                }
            }


            binding.recy.layoutManager = LinearLayoutManager(activity)
            binding.recy.adapter = adapter
            adapter.setUpRanks(items.details)
        }

        val drawable = binding.chevron
        val gpa = binding.gpaText
        val totalCh =   binding.chTotal

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

    fun calculate(){
        val _results = result[1]
        var totalCh = 0
        var totalQp = 0
        for (i in 1.._results.details.size){
            val _items: TableItems = _results.details[i]
            totalCh = totalCh.plus(_items.cH.toInt())
            totalQp = totalQp.plus(_items.qp.toInt())
        }

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






//        for (i in item.details.size){
//            val _items: TableItems = item.details[i]
//            totalCh = totalCh.plus(_items.cH.toInt())
////            totalQp = totalQp.plus(_items.qp.toInt())
//            holder.totalCh.text = totalCh.toString()
//        }
    }

    private var onItemClickListener: ((Results) -> Unit)? = null
    fun setOnItemClickListener(listener: (Results) -> Unit) {
        onItemClickListener = listener
    }


    override fun getItemCount(): Int {
        return result.size
    }

}