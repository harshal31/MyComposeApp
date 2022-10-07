package com.example.mycomposeapp.model


import com.google.gson.annotations.SerializedName

data class PostsItem(
    @SerializedName("body")
    val body: String = "", // quia et suscipitsuscipit recusandae consequuntur expedita et cumreprehenderit molestiae ut ut quas totamnostrum rerum est autem sunt rem eveniet architecto
    @SerializedName("id")
    val id: Int = 0, // 1
    @SerializedName("title")
    val title: String = "", // sunt aut facere repellat provident occaecati excepturi optio reprehenderit
    @SerializedName("userId")
    val userId: Int = 0 // 1
)