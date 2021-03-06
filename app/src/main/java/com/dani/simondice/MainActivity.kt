package com.dani.simondice

import android.media.MediaPlayer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    var secuencia = ArrayList<String>() //Secuncia en ronda actual
    var ronda = 0     //número de ronda
    var numero = 3    //número de luces encendidas
    var restante = 0  //número de comprobaciones restantes

    //Objetos para los sonidos
    lateinit var inicio: MediaPlayer
    lateinit var acierto: MediaPlayer
    lateinit var fallo: MediaPlayer
    lateinit var next: MediaPlayer

    /**
     * Guarda el estado de las variables cuando el sistema destruye la Activity y más adelante la restaura.
     */
    override fun onSaveInstanceState(guardarEstado: Bundle) {
        super.onSaveInstanceState(guardarEstado)
        guardarEstado.putInt("ronda",ronda)
        guardarEstado.putInt("numero", numero)
        guardarEstado.putInt("restante", restante)
    }

    /**
     * Restaura el estado de las variables cuando restaura la Activity
     */
    override fun onRestoreInstanceState(recEstado: Bundle) {
        super.onRestoreInstanceState(recEstado)
        ronda = recEstado.getInt("ronda")
        numero = recEstado.getInt("numero")
        restante= recEstado.getInt("restante")

        nronda.setText(ronda.toString())
        nrestante.setText(restante.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nronda.setText(ronda.toString())
        nrestante.setText(restante.toString())

        //Botón play
        bplay.setOnClickListener {
            toast("Nueva partida!")
            inicio = MediaPlayer.create(this, R.raw.inicio)
            sonido(inicio,2500)
            ronda=1
            numero=3
            restante=3
            nronda.setText(ronda.toString())
            nrestante.setText("0")
            mostrarSecuencia(numero)
        }

        //Botón azul
        bazul.setOnClickListener {
            comprobar("azul")
        }

        //Botón verde
        bverde.setOnClickListener {
            comprobar("verde")
        }

        //Botón amarillo
        bamarillo.setOnClickListener {
            comprobar("amarillo")
        }

        //Botón rojo
        brojo.setOnClickListener {
            comprobar("rojo")
        }
    }

    /**
     * Muesta una secuencia de parpadeos
     * @numero: número de parpadeos que se van a realizar
     */
    private fun mostrarSecuencia(numero: Int) {

        var encendido = 0L

        deshabilitarBotones()

        for (i in 1..numero) {
            val random = Random().nextInt(4)
            encendido+=2500L
            when (random) {
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

    /**
     * Deshabilita todos los botones
     */
    private fun deshabilitarBotones() {
        bplay.isClickable = false
        bazul.isClickable = false
        brojo.isClickable = false
        bamarillo.isClickable = false
        bverde.isClickable = false
    }

    /**
     * Habilita todos los botones
     */
    private fun habilitarBotones() {
        bplay.isClickable = true
        bazul.isClickable = true
        brojo.isClickable = true
        bamarillo.isClickable = true
        bverde.isClickable = true
    }

    /**
     * Realiza el parpadeo del botón @color
     * @encendido: Milisegundos de la secuenda a los que se va a encender la luz
     * @color: Color que se va a encender
     * @maximo: número de luces totales que se van a encender
     * @actual: número de luz actual que se va a encender respecto a @maximo de luces que se encienden
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

    /**
     * Comprueba si el usuario, a la hora de pulsar un botón, ha acertado con la siguiente luz de la secuencia.
     * Realiza una serie de operaciones según haya acertado, haya fallado o no queden comprobaciones restantes.
     * @color: Botón que pulsó el usuario
     */
    private fun comprobar(color: String){
        if(secuencia.isEmpty()==true){
            ronda=1
            numero=3
            restante=0
            toast("Presiona Play para jugar")
            fallo = MediaPlayer.create(this, R.raw.fallo)
            sonido(fallo,1000)
            secuencia.clear()
        }else{
            if(secuencia.get(0).equals(color)){
                secuencia.removeAt(0)
                toast("Acierto!")
                acierto = MediaPlayer.create(this, R.raw.acierto)
                sonido(acierto,700)
                restante--
                if(secuencia.isEmpty()==true){
                    numero++
                    ronda++
                    nronda.setText(ronda.toString())
                    next = MediaPlayer.create(this, R.raw.next)
                    runBlocking {
                        toast("Ronda: $ronda")
                        delay(500L)
                        sonido(next,1000)
                    }
                    mostrarSecuencia(numero)
                }
                nrestante.setText(restante.toString())
            }else{
                ronda=1
                numero=3
                restante=0
                toast("Ohhh...has fallado,vuelve a intentarlo")
                fallo = MediaPlayer.create(this, R.raw.fallo)
                sonido(fallo,1000)
                secuencia.clear()
            }
        }
    }

    /**
     * Recibe un sonido y lo reproduce durante un tiempo determinado
     * @sonido: MediaPlayer con el sonido que se va a reproducir
     * @tiempo: Tiempo en el cuál se va a reproducir @sonido
     */
    private fun sonido(sonido: MediaPlayer,tiempo: Long){
        sonido.start()
        GlobalScope.launch(Dispatchers.Main) {
            delay(tiempo)
            sonido.stop()
            sonido.release()
        }
    }

}
