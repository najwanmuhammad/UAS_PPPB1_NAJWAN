package com.example.uasppapbnajwan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.uasppapbnajwan.databinding.ItemAdminBinding
import com.example.uasppapbnajwan.model.Movie

class MovieAdapter(
    private val movies: List<com.example.uasppapbnajwan.model.Movie>,
    private val onEditClick: (com.example.uasppapbnajwan.model.Movie) -> Unit,
    private val onDeleteClick: (com.example.uasppapbnajwan.model.Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemAdminBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            binding.txtTitle.text = movie.judul
            binding.txtLoc.text = movie.director
            binding.txtDate.text = movie.tanggal
            Glide.with(binding.imgId.context)
                .load(movie.foto)  // event.imageResource adalah URL gambar
                .into(binding.imgId)  // Memasukkan gambar ke ImageView

            // Handle edit button click
            binding.btnEdit.setOnClickListener {
                onEditClick(movie)
            }

            // Handle delete button click
            binding.btnDelete.setOnClickListener {
                onDeleteClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemAdminBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}