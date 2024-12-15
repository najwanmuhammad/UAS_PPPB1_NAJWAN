package com.example.uasppapbnajwan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasppapbnajwan.databinding.ItemUserBinding

class UserMovieAdapter(
    private val movies: List<com.example.uasppapbnajwan.model.Movie>,
    private val onloveClick: (com.example.uasppapbnajwan.model.Movie) -> Unit,
) : RecyclerView.Adapter<UserMovieAdapter.MovieViewHolder>() {

    private var onItemClickListener: ((com.example.uasppapbnajwan.model.Movie) -> Unit)? = null

    fun setOnItemClickListener(listener: (com.example.uasppapbnajwan.model.Movie) -> Unit) {
        onItemClickListener = listener
    }

    inner class MovieViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Memastikan item bisa diklik
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(movies[adapterPosition])
            }
        }

        fun bind(movie: com.example.uasppapbnajwan.model.Movie) {
            binding.txtTitle.text = movie.judul
            binding.txtLoc.text = movie.director
            binding.txtDate.text = movie.tanggal
            Glide.with(binding.imgId.context)
                .load(movie.foto)  // movie.imageResource adalah URL gambar
                .into(binding.imgId)  // Memasukkan gambar ke ImageView
            // Handle edit button click
            binding.btnWishlist.setOnClickListener {
                onloveClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}