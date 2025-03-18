package com.example.leagueofheroes.data

import com.google.gson.annotations.SerializedName

class SuperHeroResponse (
    val response: String,
    val results: List<SuperHero>
)

class SuperHero (
    val id: String,
    val name: String,
    val biography: Biography,
    val work: Work,
    val appearance: Appearance,
    val image: Image,
    @SerializedName("powerstats") val stats: Powerstats
) {
}

class Biography (
    val publisher: String,
    @SerializedName("full-name") val realName: String,
    @SerializedName("place-of-birth") val placeOfBirth: String,
    val alignment: String
)

class Work (
    val occupation: String,
    val base: String
)

class Appearance (
    val gender: String,
    val race: String,
    @SerializedName("eye-color") val eyeColor: String,
    @SerializedName("hair-color") val hairColor: String,
    val height: List<String>,
    val weight: List<String>,
) {
    fun getWeightKg(): String {
        return weight[1]
    }

    fun getHeightCm(): String {
        return height[1]
    }
}

class Powerstats (
    val intelligence: String,
    val strength: String,
    val speed: String,
    val durability: String,
    val power: String,
    val combat: String
)
class Image (val url: String)