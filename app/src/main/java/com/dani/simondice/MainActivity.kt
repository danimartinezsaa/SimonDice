package com.dani.simondice

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val botones=arrayOf("azul","amarillo","verde","rojo")   //Colores
    val secuencia = mutableListOf<String>()                 //Secuncia en ronda actual
    var ronda=0     //número de ronda
    var numero=3    //número de luces encendidas

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Boton Play
        bplay.setOnClickListener {

            toast("Le has dado a iniciar partida")

            mostrarSecuencia(5)


        }

    }

    private fun mostrarSecuencia(numero: Int){
        val random=Random()
        var aleatorio=0

        deshabilitarBotones()

        for(i in 0..numero){
            aleatorio=random.nextInt(0..4)
            Log.d("tag","iteracion: $i")
            when(aleatorio){
                0->{
                    Log.d("tag","se enciende azul")
                    bazul.setBackgroundColor(resources.getColor(R.color.azul_encendido))
                    GlobalScope.launch {
                        delay(1500L)
                        Log.d("tag", "se apaga azul")
                        bazul.setBackgroundColor(resources.getColor(R.color.azul_apagado))
                    }
                    secuencia.add("azul")
                }
                1->{
                    Log.d("tag","se enciende amarillo")
                    bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_encendido))
                    GlobalScope.launch {
                        delay(1500L)
                        Log.d("tag","se apaga amarillo")
                        bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_apagado))
                    }
                    secuencia.add("amarillo")
                }
                2->{
                    Log.d("tag","se enciende verde")
                    bverde.setBackgroundColor(resources.getColor(R.color.verde_encendido))
                    GlobalScope.launch {
                        delay(1500L)
                        Log.d("tag","se apaga verde")
                        bverde.setBackgroundColor(resources.getColor(R.color.verde_apagado))
                    }
                    secuencia.add("verde")
                }
                3->{
                    Log.d("tag","se enciende rojo")
                    brojo.setBackgroundColor(resources.getColor(R.color.rojo_encendido))
                    GlobalScope.launch {
                        delay(1500L)
                        Log.d("tag","se apaga rojo")
                        brojo.setBackgroundColor(resources.getColor(R.color.rojo_apagado))
                    }
                    secuencia.add("rojo")
                }
            }
            Thread.sleep(2500L)
            Log.d("tag","iteracion_acabada")
        }
        Log.d("tag","bucle_acabado")
        habilitarBotones()
        secuencia.clear()   //Prueba
    }

    private fun deshabilitarBotones(){
        bplay.isClickable=false
        bazul.isClickable=false
        brojo.isClickable=false
        bamarillo.isClickable=false
        bverde.isClickable=false
    }

    private fun habilitarBotones(){
        bplay.isClickable=true
        bazul.isClickable=true
        brojo.isClickable=true
        bamarillo.isClickable=true
        bverde.isClickable=true
    }

    fun Random.nextInt(range: IntRange): Int {
        return range.start + nextInt(range.last - range.start)
    }
}
