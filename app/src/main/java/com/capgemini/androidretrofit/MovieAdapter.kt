package com.capgemini.androidretrofit

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieAdapter(val mList: List<Movie>,
                   val selectionCallback: (Movie) -> Unit)
    : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {

    class MovieHolder(v: View): RecyclerView.ViewHolder(v){
        val posterIv = v.findViewById<ImageView>(R.id.imageView)
        val titleTextView = v.findViewById<TextView>(R.id.titleT)
        val overViewTextView = v.findViewById<TextView>(R.id.overviewT)
        val rDateTextView = v.findViewById<TextView>(R.id.releaseT)
        val ratingBar = v.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieAdapter.MovieHolder {
        // inflate layout
        val v = LayoutInflater.from(parent.context).inflate(R.layout.movie_item,
            parent, false)
        return MovieHolder(v)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieHolder, position: Int) {
        // bind data
        val movie = mList[position]

        holder.titleTextView.text = movie.title
        holder.overViewTextView.text = movie.overview
        holder.ratingBar.rating = movie.vote_average.toFloat()
        holder.rDateTextView.text = movie.release_date

        val imageUrl = "https://image.tmdb.org/t/p/w500${movie.poster_path}"

        val imageUri = Uri.parse(imageUrl)

        Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.posterIv)

        holder.itemView.setOnClickListener {
            selectionCallback(movie)
        }

    }

    override fun getItemCount() = mList.size

}