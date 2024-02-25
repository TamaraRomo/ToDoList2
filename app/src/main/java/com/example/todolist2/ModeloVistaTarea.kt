package com.example.todolist2

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import android.content.SharedPreferences
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ModeloVistaTarea(application: Application) : AndroidViewModel(application)
{
    var tareaItems = MutableLiveData<MutableList<TareaItem>>()

    private val PREF_NAME = "TAREA_PREF"
    private val KEY_TAREA_LIST = "TAREA_LIST"
    private val KEY_HORA_TAREA_MAP = "HORA_TAREA_MAP"
    private val gson = Gson()

    init{
        tareaItems.value = cargarTareas()
    }

    private fun guardarTareas() {
        val prefs: SharedPreferences = getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(tareaItems.value)
        editor.putString(KEY_TAREA_LIST, json)

        val horaTareasMap = tareaItems.value?.associateBy({ it.id }, { it.horaTarea?.toString() })
        val horaTareasJson = gson.toJson(horaTareasMap)
        editor.putString(KEY_HORA_TAREA_MAP, horaTareasJson)

        editor.apply()
    }


    /*private fun cargarTareas(): MutableList<TareaItem> {
        val prefs: SharedPreferences = getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TAREA_LIST, "")
        val type: Type = object : TypeToken<MutableList<TareaItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }*/

    private fun cargarTareas(): MutableList<TareaItem> {
        val prefs: SharedPreferences = getApplication<Application>().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_TAREA_LIST, "")
        val type: Type = object : TypeToken<MutableList<TareaItem>>() {}.type

        val tareaItems = gson.fromJson<MutableList<TareaItem>>(json, type) ?: mutableListOf()

        // Recuperar y convertir las horas de tarea
        val horaTareasJson = prefs.getString(KEY_HORA_TAREA_MAP, "")
        val horaTareasMap: Map<UUID, String>? = gson.fromJson(horaTareasJson, object : TypeToken<Map<UUID, String>>() {}.type)

        horaTareasMap?.forEach { (id, horaString) ->
            val tareaItem = tareaItems.find { it.id == id }
            tareaItem?.horaTarea = horaString?.let { LocalTime.parse(it) }
        }

        return tareaItems
    }



    fun agregarTareaItem(nuevaTarea: TareaItem) {
        val list = tareaItems.value
        list!!.add(nuevaTarea)
        tareaItems.postValue(list)
        guardarTareas()
    }
    fun actualizarTareaItem(id: UUID, nombre: String, descripcion: String, horaTarea: LocalTime?) {
        val list = tareaItems.value
        val task = list!!.find { it.id == id }!!
        task.nombre = nombre
        task.descripcion= descripcion
        task.horaTarea = horaTarea
        tareaItems.postValue(list)
        guardarTareas()
    }

    fun seleccionarTareaItem(tareaItem: TareaItem) {
        val list = tareaItems.value
        val task = list!!.find { it.id == tareaItem.id }!!

        // Cambiar la propiedad seleccionada
        task.seleccionada = !task.seleccionada
        tareaItems.postValue(list)
        guardarTareas()
    }

    fun eliminarTareaItem(tareaItem: TareaItem) {
        val list = tareaItems.value
        list?.remove(tareaItem)
        tareaItems.postValue(list)
        guardarTareas()
    }

}