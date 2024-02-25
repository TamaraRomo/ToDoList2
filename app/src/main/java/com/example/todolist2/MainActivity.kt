package com.example.todolist2

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist2.HojaNuevaTarea
import com.example.todolist2.ModeloVistaTarea
import com.example.todolist2.TareaItem
import com.example.todolist2.TareaItemAdapter
import com.example.todolist2.TareaItemClickListener
import com.example.todolist2.databinding.ActivityMainBinding
import com.example.todolist2.databinding.ActivityPinBinding
import com.example.todolist2.databinding.ActivityPinPantallaBinding

class MainActivity : AppCompatActivity(), TareaItemClickListener {

    private lateinit var modeloVistaTarea: ModeloVistaTarea
    private lateinit var bindingMain: ActivityMainBinding
    private lateinit var bindingPin: ActivityPinBinding
    private lateinit var bindingPinPantalla: ActivityPinPantallaBinding

    private lateinit var sharedPreferences: SharedPreferences
    private val PIN_KEY = "PIN_KEY"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar SharedPreferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        // Verificar si es la primera vez
        val isFirstTime = !sharedPreferences.contains(PIN_KEY)

        if (isFirstTime) {
            // Mostrar la pantalla para la primera vez
            bindingPin = ActivityPinBinding.inflate(layoutInflater)
            setContentView(bindingPin.root)
            confingPantallaPinPrimeraVez()
        } else {
            // Mostrar la pantalla después de la primera vez
            bindingPinPantalla = ActivityPinPantallaBinding.inflate(layoutInflater)
            setContentView(bindingPinPantalla.root)
            configPantallaPin()
        }
    }

    private fun confingPantallaPinPrimeraVez() {
        // Configurar la lógica y listeners para la pantalla inicial
        bindingPin.btnIngresarPrimeraVez.setOnClickListener {
            val enteredPin = bindingPin.pinPrimeraVez.text.toString()

            // Validar el PIN (podrías hacer validaciones adicionales aquí)
            if (enteredPin.length == 4 && enteredPin.toIntOrNull() != null) {
                // Guardar el PIN en SharedPreferences
                sharedPreferences.edit().putString(PIN_KEY, enteredPin).apply()

                // Navegar a la pantalla después de la primera vez
                mostrarPantallaTareas()
            } else {
                // Mostrar mensaje de error o realizar alguna acción adicional
                Toast.makeText(this, "El PIN debe tener exactamente 4 dígitos.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun configPantallaPin() {
        // Configurar la lógica y listeners para la pantalla después de la primera vez
        bindingPinPantalla.btnIngresar.setOnClickListener {
            val storedPin = sharedPreferences.getString(PIN_KEY, "")

            // Verificar si el PIN ingresado coincide con el almacenado
            if (bindingPinPantalla.pinPantalla.text.toString() == storedPin) {
                // Lógica para la pantalla después de la primera vez
                mostrarPantallaTareas()
            } else {
                // Mostrar un Toast indicando que el PIN es incorrecto
                Toast.makeText(this, "PIN incorrecto. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun mostrarPantallaTareas() {
        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)

        bindingMain.btnNuevaTarea.setOnClickListener {
            HojaNuevaTarea(null).show(supportFragmentManager, "nuevaTarea")
        }

        modeloVistaTarea = ViewModelProvider(this).get(ModeloVistaTarea::class.java)
        setRecyclerView()
    }

    private fun setRecyclerView() {
        val mainActivity = this
        modeloVistaTarea.tareaItems.observe(this) {
            bindingMain.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TareaItemAdapter(it, mainActivity)
            }
        }
    }

    // Estas funciones ahora están dentro del contexto de la pantalla de tareas
    override fun editarTareaItem(tareaItem: TareaItem) {
        HojaNuevaTarea(tareaItem).show(supportFragmentManager, "nuevaTarea")
    }

    override fun seleccionarTareaItem(tareaItem: TareaItem) {
        modeloVistaTarea.seleccionarTareaItem(tareaItem)
    }

    override fun eliminarTareaItem(tareaItem: TareaItem) {
        modeloVistaTarea.eliminarTareaItem(tareaItem)
    }
}


