package kdo.one.mymoviewithpaginlibrary.data.vo


import com.google.gson.annotations.SerializedName

data class Movie(
    val id: Int,
    val posterPath: String,
    @SerializedName("release_date")
    val releaseDate: String,
    val title: String
)