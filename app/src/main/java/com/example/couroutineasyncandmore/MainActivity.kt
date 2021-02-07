package com.example.couroutineasyncandmore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.couroutineasyncandmore.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding= ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener {
            var x = 1

            lifecycleScope.launch {

                while (true) {
                    delay(1000L)
                    x++
                    println("$x")
                    launch(Dispatchers.Main) {
                        binding.number.text = x.toString()

                        /**
                         * When this counter reaches 10 the new activity will be launched and
                         * we dont have to worry about memory leaks because when the activity is
                         * destroyed all couroutines will be destroyed within the lifecycle scope.
                         * if we had GlobalScope that lives though the app then it will continue
                         * running even if we pass to the second activity.
                         */

                        if(x ==10){
                            Intent(this@MainActivity,SecondActivity::class.java).also {
                                startActivity(it)
                            }
                        }

                    }
                }

            }
        }



        GlobalScope.launch(Dispatchers.IO) {

            val time = measureTimeMillis {

                val answer1= async { networkCall1()}
                val answer2 = async { networkCall2() }

                    println("This is the network1 result ${answer1.await()}")
                    println("This is the network2 result ${answer2}")


                /**
                 * With this code the both emited the result in 6 seconds
                 * result 6 seconds lets reduce this time!
                 */
//                val answer1 = networkCall1()
//                val answer2= networkCall2()
//
//                println("This is the answer1: $answer1")
//                println("This is the answer2:$answer2")

            }

            println("This is the time it took: $time")

        }
    }


    suspend fun networkCall1():String {
        delay(3000L)
        return "network1"
    }
    suspend fun networkCall2():String {
        delay(3000L)
        return " network2"
    }
}