package com.capgemini.androidretrofit

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbInterface {

    // baseurl/movie/popular?api_key="asjkdfj"
    
    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") aKey: String) : Call<PopularMovies>

    /*
    getMovieDetails(10, "a2734834")

    baseurl/movie/10?api_key=a2734834
     */

    @GET("movie/{movieId}")
    fun getMovieDetails(@Path("movieId") id: Int,
                        @Query("api_key") aKey: String) : Call<MovieDetails>


    companion object {

        private val TMDB_BASE_URL = "https://api.themoviedb.org/3/"

        fun getInstance() : TmdbInterface {
            val builder = Retrofit.Builder()
            builder.baseUrl(TMDB_BASE_URL)
            builder.addConverterFactory(GsonConverterFactory.create())

            val retro = builder.build()
            return retro.create(TmdbInterface::class.java)
        }

    }

}