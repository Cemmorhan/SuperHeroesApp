package com.example.superheroesapp.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.leagueofheroes.data.SuperHero
import com.example.superheroesapp.R
import com.example.superheroesapp.data.SuperheroService
import com.example.superheroesapp.databinding.ActivityDetailBinding
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailActivity : AppCompatActivity() {
    lateinit var progressBar: LinearProgressIndicator
    lateinit var binding: ActivityDetailBinding
    lateinit var superhero: SuperHero

    lateinit var intelligence: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra("SUPERHERO_ID")!!
        getSuperheroById(id)


        binding.navigationBar.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_biography -> {
                    binding.appearanceContent.root.visibility = View.GONE
                    binding.statsContent.root.visibility = View.GONE
                    binding.biographyContent.root.visibility = View.VISIBLE
                }

                R.id.action_appearance -> {
                    binding.statsContent.root.visibility = View.GONE
                    binding.biographyContent.root.visibility = View.GONE
                    binding.appearanceContent.root.visibility = View.VISIBLE
                }

                R.id.action_stats -> {
                    binding.biographyContent.root.visibility = View.GONE
                    binding.appearanceContent.root.visibility = View.GONE
                    binding.statsContent.root.visibility = View.VISIBLE
                }
            }
            true
        }

        binding.navigationBar.selectedItemId = R.id.action_biography
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //dar funcion a la flecha de atras
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getRetrofit(): SuperheroService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.superheroapi.com/api/dbc2e43ab98b528a958fda749b830fa3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(SuperheroService::class.java)
    }

    private fun loadData() {
        Picasso.get().load(superhero.image.url).into(binding.pictureImageView)
        //inicializar la barra de carga
        progressBar = findViewById(R.id.progressBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = superhero.name
        supportActionBar?.subtitle = superhero.biography.realName

        // Biography
        binding.biographyContent.publisherTextView.text = superhero.biography.publisher
        binding.biographyContent.placeOfBirthTextView.text = superhero.biography.placeOfBirth
        binding.biographyContent.alignmentTextView.text = superhero.biography.alignment

        binding.biographyContent.occupationTextView.text = superhero.work.occupation
        binding.biographyContent.baseTextView.text = superhero.work.base

        // Appearance
        binding.appearanceContent.raceTextView.text = superhero.appearance.race
        binding.appearanceContent.genderTextView.text = superhero.appearance.gender
        binding.appearanceContent.eyeColorTextView.text = superhero.appearance.eyeColor
        binding.appearanceContent.hairColorTextView.text = superhero.appearance.hairColor
        binding.appearanceContent.weightTextView.text = superhero.appearance.getWeightKg()
        binding.appearanceContent.heightTextView.text = superhero.appearance.getHeightCm()

        // Stats
        /*
        con with nos ahorramos poner superhero.stats muchas veces porque estamos dentro de stats. (no dentro del detailActivity)
        with (superhero.stats) {
            binding.statsContent.inteligenceTextView.text = intelligence
            binding.statsContent.stretchTextView.text = strength
            binding.statsContent.speedTextView.text = speed
            binding.statsContent.durabilityTextView.text = durability
            binding.statsContent.powerTextView.text = power
            binding.statsContent.combatTextView.text = combat
         */

        binding.statsContent.inteligenceTextView.text = "${tryConvertToInt(superhero.stats.intelligence)}"
        binding.statsContent.stretchTextView.text = "${tryConvertToInt(superhero.stats.strength)}"
        binding.statsContent.speedTextView.text = "${tryConvertToInt(superhero.stats.speed)}"
        binding.statsContent.durabilityTextView.text = "${tryConvertToInt(superhero.stats.durability)}"
        binding.statsContent.powerTextView.text = "${tryConvertToInt(superhero.stats.power)}"
        binding.statsContent.combatTextView.text = "${tryConvertToInt(superhero.stats.combat)}"

        //Con comprobación anti nulls

        binding.statsContent.intelligenceProgressBar.progress = superhero.stats.intelligence.toIntOrNull() ?: 0
        binding.statsContent.strengthProgressBar.progress = superhero.stats.strength.toIntOrNull() ?: 0
        binding.statsContent.speedProgressBar.progress = superhero.stats.speed.toIntOrNull() ?: 0
        binding.statsContent.durabilityProgressBar.progress = superhero.stats.durability.toIntOrNull() ?: 0
        binding.statsContent.powerProgressBar.progress = superhero.stats.power.toIntOrNull() ?: 0
        binding.statsContent.combatProgressBar.progress = superhero.stats.combat.toIntOrNull() ?: 0

        //control de color de la barra de stats
        changeColorProgressBar(binding.statsContent.intelligenceProgressBar, tryConvertToInt(superhero.stats.intelligence))
        changeColorProgressBar(binding.statsContent.strengthProgressBar, tryConvertToInt(superhero.stats.strength))
        changeColorProgressBar(binding.statsContent.speedProgressBar, tryConvertToInt(superhero.stats.speed))
        changeColorProgressBar(binding.statsContent.durabilityProgressBar, tryConvertToInt(superhero.stats.durability))
        changeColorProgressBar(binding.statsContent.powerProgressBar, tryConvertToInt(superhero.stats.power))
        changeColorProgressBar(binding.statsContent.combatProgressBar, tryConvertToInt(superhero.stats.combat))


        //quitar barra de carga
        progressBar.visibility = ProgressBar.GONE

    }
    fun changeColorProgressBar(progressBar: ProgressBar, value: Int) {
        when (value) {
            0 -> progressBar.progressTintList = getColorStateList(R.color.gris)
            in 1..33 -> progressBar.progressTintList = getColorStateList(R.color.red)
            in 34..66 -> progressBar.progressTintList = getColorStateList(R.color.yellow)
            in 67..100 -> progressBar.progressTintList = getColorStateList(R.color.green)
        }
    }
    fun tryConvertToInt(valor: String): Int {
        try {
            return valor.toInt()
        } catch (e: Exception) {
            return 0
        }
    }

    fun getSuperheroById(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = getRetrofit()
                superhero = service.findSuperheroById(id)

                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}