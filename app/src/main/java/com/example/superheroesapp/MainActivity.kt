package com.example.superheroesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        getRetrofit()
    }
    fun getRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.superheroapi.com/api/dbc2e43ab98b528a958fda749b830fa3")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SuperheroService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val result = service.findSuperheroesByName("super")
            for (superhero in result.result) {
                Log.i("Superhero", "${superhero.id} -> ${superhero.name}")
            }
        }
    }
}