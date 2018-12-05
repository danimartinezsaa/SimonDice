package com.dani.simondice

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    val secuencia = mutableListOf<String>() //Secuncia en ronda actual
    var ronda = 0     //número de ronda
    var numero = 3    //número de luces encendidas
    var restante = 0  //número de comprobaciones restantes

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nronda.setText("0")
        nrestante.setText("0")

        bplay.setOnClickListener {

            toast("Nueva partida!")
            val mp = MediaPlayer.create(this, R.raw.inicio)
            mp.start()
            ronda=1
            numero=3
            restante=3
            nronda.setText(ronda.toString())
            nrestante.setText("0")
            mostrarSecuencia(numero)

        }

        bazul.setOnClickListener {
            comprobar("azul")
        }

        bverde.setOnClickListener {
            comprobar("verde")
        }

        bamarillo.setOnClickListener {
            comprobar("amarillo")
        }

        brojo.setOnClickListener {
            comprobar("rojo")
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
                    parpadeo(encendido,"azul",numero,i)
                    secuencia.add("azul")
                }
                1 -> {
                    parpadeo(encendido,"amarillo",numero,i)
                    secuencia.add("amarillo")
                }
                2 -> {
                    parpadeo(encendido,"verde",numero,i)
                    secuencia.add("verde")
                }
                3 -> {
                    parpadeo(encendido,"rojo",numero,i)
                    secuencia.add("rojo")
                }
            }
        }
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
    private fun parpadeo(encendido: Long, color: String,maximo: Int,actual: Int) {
        GlobalScope.launch(Dispatchers.Main) {
            delay(encendido)
            when (color) {
                "azul" -> {
                    bazul.setBackgroundColor(resources.getColor(R.color.azul_encendido))
                    delay(1000L)
                    bazul.setBackgroundColor(resources.getColor(R.color.azul_apagado))
                }
                "amarillo" -> {
                    bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_encendido))
                    delay(1000L)
                    bamarillo.setBackgroundColor(resources.getColor(R.color.amarillo_apagado))
                }
                "verde" -> {
                    bverde.setBackgroundColor(resources.getColor(R.color.verde_encendido))
                    delay(1000L)
                    bverde.setBackgroundColor(resources.getColor(R.color.verde_apagado))
                }
                "rojo" -> {
                    brojo.setBackgroundColor(resources.getColor(R.color.rojo_encendido))
                    delay(1000L)
                    brojo.setBackgroundColor(resources.getColor(R.color.rojo_apagado))
                }
            }
            if(actual>=maximo){
                habilitarBotones()
                toast("Reproduce la secuencia")
                restante=numero
                nrestante.setText(restante.toString())

            }
        }
    }

    /*
     * Devuelve si el usuario ha acertado o no
     */
    private fun comprobar(color: String){
        if(secuencia.isEmpty()==true){
            ronda=1
            numero=3
            restante=0
            toast("Ohhh...has fallado,vuelve a intentarlo")
            val mp = MediaPlayer.create(this, R.raw.fallo)
            mp.start()
            secuencia.clear()
        }else{
            if(secuencia.get(0).equals(color)){
                secuencia.removeAt(0)
                toast("Acierto!")
                val mp = MediaPlayer.create(this, R.raw.acierto)
                mp.start()
                restante--
                if(secuencia.isEmpty()==true){
                    numero++
                    ronda++
                    nronda.setText(ronda.toString())
                    runBlocking {     // but this expression blocks the main thread
                        delay(1000L)  // ... while we delay for 2 seconds to keep JVM alive
                    }
                    toast("Ronda: $ronda")
                    val mp = MediaPlayer.create(this, R.raw.next)
                    mp.start()
                    mostrarSecuencia(numero)
                }
                nrestante.setText(restante.toString())
            }else{
                ronda=1
                numero=3
                restante=0
                toast("Ohhh...has fallado,vuelve a intentarlo")
                val mp = MediaPlayer.create(this, R.raw.fallo)
                mp.start()
                secuencia.clear()
            }
        }
    }

}
