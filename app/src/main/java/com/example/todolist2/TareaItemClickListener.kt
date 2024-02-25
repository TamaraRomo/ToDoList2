package com.example.todolist2

interface TareaItemClickListener {

    fun editarTareaItem(tareaItem: TareaItem)
    fun seleccionarTareaItem(tareaItem: TareaItem)
    fun eliminarTareaItem(tareaItem: TareaItem)
}