package com.example.uasppapbnajwan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasppapbnajwan.database.AppDatabase
import com.example.uasppapbnajwan.database.MovieEntity
import com.example.uasppapbnajwan.databinding.FragmentUserFavoritesBinding
import com.example.uasppapbnajwan.model.Movie
import com.example.uasppapbnajwan.user.UserDetailMovieActivity
import kotlinx.coroutines.launch


class UserFavoritesFragment : Fragment() {

    private var _binding: FragmentUserFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: UserMovieAdapter // Mendeklarasikan adapter sebagai properti fragment
    private var favoriteMovies: MutableList<Movie> = mutableListOf() // List mutable untuk movie favorit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserFavoritesBinding.inflate(inflater, container, false)

        // Menampilkan data favorit dari Room
        showFavoriteMovies()

        return binding.root
    }

    private fun showFavoriteMovies() {
        val db = AppDatabase.getDatabase(requireContext()) // Inisialisasi database
        val movieDao = db.movieDao()

        lifecycleScope.launch {
            // Ambil data favorit dari Room dan konversi ke List<Movie>
            val favoriteMoviesFromDb = movieDao.getAllMovies().map { convertToMovie(it) }
            Log.d("UserFavoritesFragment", "Favorite movies size: ${favoriteMoviesFromDb.size}")
            favoriteMoviesFromDb.forEach {
                Log.d("UserFavoritesFragment", "Movie: ${it.judul}")
                Log.d("UserFavoritesFragment", "Image URL: ${it.foto}")
            }

            // Cek jika favoriteMovies memiliki data
            if (favoriteMoviesFromDb.isEmpty()) {
                Log.d("UserFavoritesFragment", "No favorite movies found")
            }

            // Update favoriteMovies list
            favoriteMovies.clear()
            favoriteMovies.addAll(favoriteMoviesFromDb)

            // Inisialisasi adapter dengan favoriteMovies dan onloveClick
            adapter = UserMovieAdapter(favoriteMovies) { movie ->
                lifecycleScope.launch {
                    // Menghapus movie dari Room
                    movieDao.deleteMovieById(movie.id) // Menghapus berdasarkan ID
                    // Update list favoriteMovies setelah penghapusan
                    favoriteMovies.remove(movie)
                    // Notifikasi adapter bahwa data telah berubah
                    adapter.notifyDataSetChanged() // Memperbarui tampilan
                }
            }

            // Set listener untuk klik item card
            adapter.setOnItemClickListener { movie ->
                val intent = Intent(requireContext(), UserDetailMovieActivity::class.java)
                // Mengirim data ke Activity UserDetailAcara
                intent.putExtra("judul", movie.judul)
                intent.putExtra("director", movie.director)
                intent.putExtra("tanggal", movie.tanggal)
                intent.putExtra("deskripsi", movie.deskripsi)
                intent.putExtra("foto", movie.foto)
                startActivity(intent)
            }

            // Mengatur RecyclerView
            binding.cardUser.apply {
                layoutManager = GridLayoutManager(requireContext(), 2) // 2 kolom
                this.adapter = this@UserFavoritesFragment.adapter
            }

            Log.d("UserFavoritesFragment", "RecyclerView adapter set with ${favoriteMovies.size} items.")
        }
    }


    // Fungsi untuk konversi MovieEntity ke Movie
    fun convertToMovie(movieEntity: MovieEntity): Movie {
        return Movie(
            movieEntity.id,
            movieEntity.judul,
            movieEntity.director,
            movieEntity.tanggal,
            movieEntity.deskripsi,
            movieEntity.foto
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding ketika view dihancurkan
    }
}