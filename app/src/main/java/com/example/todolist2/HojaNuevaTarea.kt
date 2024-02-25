package com.example.todolist2

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.todolist2.databinding.FragmentHojaNuevaTareaBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class HojaNuevaTarea(var tareaItem: TareaItem?) : BottomSheetDialogFragment()
{

    private lateinit var binding: FragmentHojaNuevaTareaBinding
    private lateinit var modeloVistaTarea: ModeloVistaTarea
    private var horaTarea: LocalTime? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        if(tareaItem != null)
        {
            binding.tareaTitulo.text = "Editar tarea"
            val editable = Editable.Factory.getInstance()
            binding.nombre.text = editable.newEditable(tareaItem!!.nombre)
            binding.descripcion.text = editable.newEditable(tareaItem!!.descripcion)
            if(tareaItem!!.horaTarea != null){
                horaTarea = tareaItem!!.horaTarea!!
                btnActualizarTiempoTexto()
            }
        }
        else{
            binding.tareaTitulo.text = "Nueva tarea"
        }

        modeloVistaTarea = ViewModelProvider(activity).get(ModeloVistaTarea::class.java)
        binding.btnGuadar.setOnClickListener{
            guardarAccion()
        }
        binding.btnTimerPicker.setOnClickListener{
            tiempoPicker()
        }
        binding.btnEliminar.setOnClickListener{
            eliminarTarea()
        }
    }

    private fun tiempoPicker() {
        if(horaTarea == null)
            horaTarea = LocalTime.now()
        val listener = TimePickerDialog.OnTimeSetListener{_, horaSeleccionada, minutoSeleccionado ->
            horaTarea = LocalTime.of(horaSeleccionada, minutoSeleccionado)
            btnActualizarTiempoTexto()

        }
        val dialog = TimePickerDialog(activity, listener, horaTarea!!.hour, horaTarea!!.minute, true)
        dialog.setTitle("Hora tarea")
        dialog.show()
    }

    private fun btnActualizarTiempoTexto() {
        binding.btnTimerPicker.text = String.format("%02d:%02d", horaTarea!!.hour, horaTarea!!.minute)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHojaNuevaTareaBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun guardarAccion()
    {
        val nombre = binding.nombre.text.toString()
        val descripcion = binding.descripcion.text.toString()
        if(tareaItem == null)
        {
            val nuevaTarea = TareaItem(nombre, descripcion, horaTarea, null)
            modeloVistaTarea.agregarTareaItem(nuevaTarea)
        }else
        {
            modeloVistaTarea.actualizarTareaItem(tareaItem!!.id, nombre, descripcion, horaTarea)
        }
        binding.nombre.setText("")
        binding.descripcion.setText("")
        dismiss()
    }

    private fun eliminarTarea() {
        if (tareaItem != null) {
            modeloVistaTarea.eliminarTareaItem(tareaItem!!)
            dismiss()
        }

}}