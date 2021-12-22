package com.example.maipady.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.maipady.databinding.TableItemsBinding
import com.example.maipady.models.TableItems
import java.util.*

class TableAdapter : RecyclerView.Adapter<TableAdapter.RankingViewHolder>() {

    private val items = mutableListOf<TableItems>()

    inner class RankingViewHolder(private val binding: TableItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindItems(item: TableItems) {
            binding.sN.text = item.sN
            binding.courses.setText(item.courses)
            binding.grades.setText(item.grades)
            binding.cH.setText(item.cH)

            when {
                item.grades.uppercase(Locale.getDefault())  == "A" -> {

                    item.qp = item.cH.toInt().times(5).toString()
//                    binding.qP.setText(items.cH.toInt().times(5).toString())
                }
                item.grades.uppercase(Locale.getDefault())  == "B" -> {
                    item.qp = item.cH.toInt().times(4).toString()
                }
                item.grades.uppercase(Locale.getDefault()) == "C" -> {
                    item.qp = item.cH.toInt().times(3).toString()
                }
                item.grades.uppercase(Locale.getDefault())  == "D" -> {
                    item.qp = item.cH.toInt().times(2).toString()
                }
                item.grades.uppercase(Locale.getDefault())  == "E" -> {
                    item.qp = item.cH.toInt().times(1).toString()
                }
                item.grades.uppercase(Locale.getDefault())  == "F" -> {
                    item.qp = item.cH.toInt().times(0).toString()
                }
            }
            binding.qP.setText(item.qp)
        }


    }
    fun totalQp(): Int {
        var total = 0
        items.forEach {
            total = total.plus(it.qp.toInt())
            return@forEach
        }
        return total
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
        items[position].qp = item.qp.toString()
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