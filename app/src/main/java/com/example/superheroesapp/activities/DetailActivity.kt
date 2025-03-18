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
        binding.statsContent.inteligenceTextView.text = superhero.stats.intelligence
        binding.statsContent.stretchTextView.text = superhero.stats.strength
        binding.statsContent.speedTextView.text = superhero.stats.speed
        binding.statsContent.durabilityTextView.text = superhero.stats.durability
        binding.statsContent.powerTextView.text = superhero.stats.power
        binding.statsContent.combatTextView.text = superhero.stats.combat

        //falta hacer la comprobación del null

        binding.statsContent.intelligenceProgressBar.progress = superhero.stats.intelligence.toInt()
        binding.statsContent.strengthProgressBar.progress = superhero.stats.strength.toInt()
        binding.statsContent.speedProgressBar.progress = superhero.stats.speed.toInt()
        binding.statsContent.durabilityProgressBar.progress = superhero.stats.durability.toInt()
        binding.statsContent.powerProgressBar.progress = superhero.stats.power.toInt()
        binding.statsContent.combatProgressBar.progress = superhero.stats.combat.toInt()

        //quitar barra de carga
        progressBar.visibility = ProgressBar.GONE

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