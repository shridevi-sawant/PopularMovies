package com.capgemini.androidretrofit

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailActivity : AppCompatActivity() {

    lateinit var fab: FloatingActionButton
    lateinit var titleTextView: TextView
    lateinit var statusTextView: TextView
    lateinit var releaseTextView: TextView
    lateinit var tagTextView: TextView
    lateinit var overviewTextView: TextView
    lateinit var posterImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        supportActionBar?.hide()

        setupUI()

        val movieId = intent.getIntExtra("id", 0)

        if(movieId != 0){
            // execute the request
            val key = resources.getString(R.string.apiKey)

            TmdbInterface.getInstance()
                .getMovieDetails(movieId, key)
                .enqueue(MovieDetailsCallback())
        }
    }

    private fun setupUI() {
        posterImageView = findViewById(R.id.iView)
        statusTextView = findViewById(R.id.statusT)
        overviewTextView = findViewById(R.id.overViewT)
        fab = findViewById(R.id.fab)
        tagTextView = findViewById(R.id.tagT)
        titleTextView = findViewById(R.id.mTitleT)
        releaseTextView = findViewById(R.id.rdateT)
    }

    inner class MovieDetailsCallback: Callback<MovieDetails> {
        override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {

            if(response.isSuccessful){
                val details = response.body()
                Toast.makeText(this@MovieDetailActivity,
                    "Got Details: ${details?.title}", Toast.LENGTH_LONG).show()

                Log.d("MovieDetailActivity", "Movie : $details")

                details?.let {
                    statusTextView.text = it.status
                    overviewTextView.text = it.overview
                    tagTextView.text = it.tagline
                    titleTextView.text = it.title
                    releaseTextView.text = "${it.revenue} USD"

                }

                val imageUrl = "https://image.tmdb.org/t/p/w500${details?.poster_path}"

                val imageUri = Uri.parse(imageUrl)

                Glide.with(this@MovieDetailActivity)
                    .load(imageUri)
                    .into(posterImageView)


                fab.setOnClickListener {
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(details?.homepage))
                    startActivity(i)
                }


            }
            else {
                Toast.makeText(this@MovieDetailActivity,
                    "Problem fetching details", Toast.LENGTH_LONG).show()

            }

        }

        override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
            Toast.makeText(this@MovieDetailActivity,
                "Some error..retry again", Toast.LENGTH_LONG).show()

        }

    }

}