package com.example.todolist2

import android.content.Context
import androidx.core.content.ContextCompat
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

data class TareaItem(
    var nombre: String,
    var descripcion: String,
    var horaTarea: LocalTime?,
    var tareaCompletada: LocalDate?,
    var id: UUID = UUID.randomUUID(),
    var seleccionada: Boolean = false
): Serializable {

    fun completada() = tareaCompletada != null
    fun imagenRecurso(): Int = if(completada()) R.drawable.checked_24 else R.drawable.unchecked__24
    fun imagenColor(context: Context): Int = if(completada()) blue(context) else black(context)

    private fun blue(context: Context) = ContextCompat.getColor(context, R.color.blue)
    private fun black(context: Context) = ContextCompat.getColor(context, R.color.black)

}