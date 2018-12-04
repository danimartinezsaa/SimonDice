package com.dani.simondice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.jetbrains.anko.UI
import java.util.*


class MainActivity : AppCompatActivity() {

    val secuencia = mutableListOf<String>()                 //Secuncia en ronda actual
    var ronda = 0     //número de ronda
    var numero = 3    //número de luces encendidas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Boton Play
        bplay.setOnClickListener {

            toast("Le has dado a iniciar partida")

            mostrarSecuencia(numero)


        }

    }

    /*
    Muesta una secuencia de parpadeos
    @numero: número de parpadeos
     */
    private fun mostrarSecuencia(numero: Int) {
        val random = Random()
        var aleatorio = 0
        var encendido = 0L

        deshabilitarBotones()

        for (i in 1..numero) {
            aleatorio = random.nextInt(4)
            encendido+=2500L
            when (aleatorio) {
                0 -> {
                    parpadeo(encendido,"azul")
                    secuencia.add("azul")
                }
                1 -> {
                    parpadeo(encendido,"amarillo")
                    secuencia.add("amarillo")
                }
                2 -> {
                    parpadeo(encendido,"verde")
                    secuencia.add("verde")
                }
                3 -> {
                    parpadeo(encendido,"rojo")
                    secuencia.add("rojo")
                }
            }
        }

        habilitarBotones()
        secuencia.clear()   //Prueba
    }
    /*
    Deshabilitar botones
    */
    private fun deshabilitarBotones() {
        bplay.isClickable = false
        bazul.isClickable = false
        brojo.isClickable = false
        bamarillo.isClickable = false
        bverde.isClickable = false
    }
    /*
    Habilitar botones
     */
    private fun habilitarBotones() {
        bplay.isClickable = true
        bazul.isClickable = true
        brojo.isClickable = true
        bamarillo.isClickable = true
        bverde.isClickable = true
    }

    /*
    Realiza el parpadeo de un botón
    @encendido: Milisegundos de la secuenda a los que se va a encender la luz
    @color: Color que se va a encender
     */
    private fun parpadeo(encendido: Long, color: String) {
        when (color) {
            "azul" -> {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(encendido)
                    bazul.setBackgroundColor(resources.getColor(R.color.azul_encendido))
                    delay(1000L)
                    bazul.setBackgroundColor(resources.getColor(R.color.azul_apagado))
                }
                secuencia.add("azul")
            }
            "amarillo" -> {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(encendido)
                    bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_encendido))
                    delay(1000L)
                    bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_apagado))
                }
                secuencia.add("amarillo")
            }
            "verde" -> {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(encendido)
                    bverde.setBackgroundColor(resources.getColor(R.color.verde_encendido))
                    delay(1000L)
                    bverde.setBackgroundColor(resources.getColor(R.color.verde_apagado))
                }
                secuencia.add("verde")
            }
            "rojo" -> {
                GlobalScope.launch(Dispatchers.Main) {
                    delay(encendido)
                    brojo.setBackgroundColor(resources.getColor(R.color.rojo_encendido))
                    delay(1000L)
                    brojo.setBackgroundColor(resources.getColor(R.color.rojo_apagado))
                }
                secuencia.add("rojo")
            }
        }
    }
}
