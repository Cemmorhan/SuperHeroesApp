package com.example.superheroesapp

class SuperHeroResponse (
    val response: String,
    val result: List<SuperHero>
){

}

class SuperHero (
    var id: String,
    var name: String
){

}