package kdo.one.mymoviewithpaginlibrary.data.api

import io.reactivex.Single
import kdo.one.mymoviewithpaginlibrary.data.vo.MovieDetails
import kdo.one.mymoviewithpaginlibrary.data.vo.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetails>

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int): Single<MoviesResponse>
}