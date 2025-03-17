package com.example.superheroesapp.data

import com.example.leagueofheroes.data.SuperHero
import com.example.leagueofheroes.data.SuperHeroResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface SuperheroService {
    //funcion para obtener los superheroes por nombre
    // el name se pone entre llaves para que sea un parametro dinamico
    @GET("search/{name}")
    suspend fun findSuperheroesByName (@Path("name") query: String): SuperHeroResponse

    //funcion para obtener los superheroes por id
    @GET("{id}")
    suspend fun findSuperheroById (@Path("id") id: String): SuperHero

}