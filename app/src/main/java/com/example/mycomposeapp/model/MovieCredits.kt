package com.example.mycomposeapp.model


import com.google.gson.annotations.SerializedName

data class MovieCredits(
    @SerializedName("cast")
    val cast: List<Cast> = listOf(),
    @SerializedName("crew")
    val crew: List<Crew> = listOf(),
    @SerializedName("id")
    val id: Int = 0 // 550
)

data class Cast(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("cast_id")
    val castId: Int = 0, // 4
    @SerializedName("character")
    val character: String = "", // The Narrator
    @SerializedName("credit_id")
    val creditId: String = "", // 52fe4250c3a36847f80149f3
    @SerializedName("gender")
    val gender: Int = 0, // 2
    @SerializedName("id")
    val id: Int = 0, // 819
    @SerializedName("known_for_department")
    val knownForDepartment: String = "", // Acting
    @SerializedName("name")
    val name: String = "", // Edward Norton
    @SerializedName("order")
    val order: Int = 0, // 0
    @SerializedName("original_name")
    val originalName: String = "", // Edward Norton
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 7.861
    @SerializedName("profile_path")
    val profilePath: String = "" // /5XBzD5WuTyVQZeS4VI25z2moMeY.jpg
)

data class Crew(
    @SerializedName("adult")
    val adult: Boolean = false, // false
    @SerializedName("credit_id")
    val creditId: String = "", // 55731b8192514111610027d7
    @SerializedName("department")
    val department: String = "", // Production
    @SerializedName("gender")
    val gender: Int = 0, // 2
    @SerializedName("id")
    val id: Int = 0, // 376
    @SerializedName("job")
    val job: String = "", // Executive Producer
    @SerializedName("known_for_department")
    val knownForDepartment: String = "", // Production
    @SerializedName("name")
    val name: String = "", // Arnon Milchan
    @SerializedName("original_name")
    val originalName: String = "", // Arnon Milchan
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 1.702
    @SerializedName("profile_path")
    val profilePath: String = "" // /b2hBExX4NnczNAnLuTBF4kmNhZm.jpg
)