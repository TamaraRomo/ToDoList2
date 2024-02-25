package com.example.todolist2

import android.content.*
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist2.databinding.TareaItemCellBinding
import java.time.format.DateTimeFormatter

class TareaItemViewHolder(
    private val context: Context,
    private val binding: TareaItemCellBinding,
    private val clickListener: TareaItemClickListener
): RecyclerView.ViewHolder(binding.root)
{
    val tiempoFormato = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTareaItem(tareaItem: TareaItem)
    {
        binding.nombre.text = tareaItem.nombre
        binding.btnCompletado.setImageResource(tareaItem.imagenRecurso())
        binding.btnCompletado.setColorFilter(tareaItem.imagenColor(context))

        binding.btnCompletado.setOnClickListener{
            clickListener.seleccionarTareaItem(tareaItem)
        }

        binding.tareaCellContenedor.setOnClickListener {
            clickListener.editarTareaItem(tareaItem)
        }

        // Nueva lógica para seleccionar/deseleccionar tarea
        binding.tareaCellContenedor.setOnLongClickListener {
            clickListener.seleccionarTareaItem(tareaItem)
            true
        }

        // Nueva lógica para mostrar la selección
        if (tareaItem.seleccionada) {
            binding.btnCompletado.setImageResource(R.drawable.checked_24)

        } else {
            binding.btnCompletado.setImageResource(R.drawable.unchecked__24)

        }

        /*if(tareaItem.horaTarea != null)
            binding.horaTarea.text = tiempoFormato.format(tareaItem.horaTarea)
        else
            binding.horaTarea.text = ""*/
        // Mostrar la hora si está presente
        if (tareaItem.horaTarea != null) {
            binding.horaTarea.text = tiempoFormato.format(tareaItem.horaTarea)
            binding.horaTarea.visibility = View.VISIBLE
        } else {
            binding.horaTarea.visibility = View.GONE
        }
    }
}