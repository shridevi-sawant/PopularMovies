package com.capgemini.androidretrofit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var rView: RecyclerView

    val movieList = mutableListOf<Movie>()
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rView = findViewById(R.id.list)
        setupRecyclerView()

        // execute the request - synchronously
        // async - thread
        val key = resources.getString(R.string.apiKey)

        // asynchronous execution
        TmdbInterface.getInstance()
            .getPopularMovies(key)
            .enqueue(MovieCallback())
    }

    private fun setupRecyclerView() {
        rView.layoutManager = LinearLayoutManager(this)
        movieAdapter = MovieAdapter(movieList, ::movieSelectionDone)
        rView.adapter = movieAdapter
    }

    fun movieSelectionDone(m : Movie){
        Toast.makeText(this, "Selected Movie: ${m.title}, ${m.id}",
            Toast.LENGTH_LONG).show()

        val i = Intent(this, MovieDetailActivity::class.java)
        i.putExtra("id", m.id)
        startActivity(i)

    }

    inner class MovieCallback : Callback<PopularMovies>{
        override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {

            if(response.isSuccessful){
                val pMovies = response.body()
                pMovies?.results?.let {
                    movieList.addAll(it)
                    Log.d("MainActivity", "List: $movieList")
                    Log.d("MainActivity", "Size: ${movieList.size}")
                    Toast.makeText(this@MainActivity,
                        "Got the list", Toast.LENGTH_LONG).show()
                    movieAdapter.notifyDataSetChanged()
                }
            }
            else{
                Toast.makeText(this@MainActivity,
                    "Some Problem..try later..", Toast.LENGTH_LONG).show()
                Log.d("MainActivity", "Code: ${response.code()}")
                Log.d("MainActivity", "Message: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
            Toast.makeText(this@MainActivity,
                "Could not get the list: ${t.message}", Toast.LENGTH_LONG).show()
        }

    }
}