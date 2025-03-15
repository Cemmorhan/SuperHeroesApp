package com.example.superheroesapp.data


class SuperHeroResponse (
    val response: String,
    val results: List<SuperHero>
){

}

class SuperHero (
    var id: String,
    var name: String,
    var image: Image,
){

}
class Image (var url: String)