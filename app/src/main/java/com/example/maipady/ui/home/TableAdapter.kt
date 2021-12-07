package com.example.maipady.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.databinding.TableItemsBinding
import com.example.maipady.models.TableItems

class TableAdapter : RecyclerView.Adapter<TableAdapter.RankingViewHolder>() {

    private val items = mutableListOf<TableItems>()

    inner class RankingViewHolder(private val binding: TableItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(items: TableItems) {
            binding.sN.text = items.sN
            binding.courses.setText(items.courses)
            binding.grades.setText(items.grades)
            binding.cH.setText(items.cH)
        }


//        fun setPosition(state: Boolean) {
//            if (state) {
//                binding.layout.setPadding(0, 0, 35, 0)
//                binding.layout1.setPadding(35, 5, 75, 5)
//            } else {
//                binding.layout.setPadding(35, 0, 0, 0)
//                binding.layout1.setPadding(5, 5, 110, 5)
//            }
//        }

    }

    fun setUpRanks(items: List<TableItems>) {
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        return RankingViewHolder(
            TableItemsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val item = items[position]
        item.sN = position.plus(1).toString()
        holder.bindItems(item)
//        if (position % 2 == 0) {
//            holder.setPosition(true)
//            holder.bindRanks(rank)
//        } else {
//            holder.setPosition(false)
//            holder.bindRanks(rank)
//        }
//        holder.viewDetails.setOnClickListener {
//            onItemClickListener?.let { it(rank) }
//        }
    }
    private var onItemClickListener: ((TableItems) -> Unit)? = null
    fun setOnItemClickListener(listener: (TableItems) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return items.size
    }

}