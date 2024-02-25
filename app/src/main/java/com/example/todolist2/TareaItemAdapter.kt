package com.example.todolist2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist2.databinding.*

class TareaItemAdapter(
    private val tareaItems: List<TareaItem>,
    private val clickListener: MainActivity

): RecyclerView.Adapter<TareaItemViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaItemViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = TareaItemCellBinding.inflate(from, parent, false)
        return TareaItemViewHolder(parent.context, binding, clickListener)
    }

    override fun getItemCount(): Int = tareaItems.size
    override fun onBindViewHolder(holder: TareaItemViewHolder, position: Int) {
        holder.bindTareaItem(tareaItems[position])
    }
}
