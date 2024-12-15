package com.example.uasppapbnajwan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasppapbnajwan.admin.AdminAddMovieActivity
import com.example.uasppapbnajwan.admin.AdminEditMovieActivity
import com.example.uasppapbnajwan.databinding.FragmentAdminKelolaMovieBinding
import com.example.uasppapbnajwan.model.Movie
import com.example.uasppapbnajwan.network.ApiClient
import retrofit2.Call
import retrofit2.Response

class AdminKelolaMovieFragment : Fragment() {

    private var _binding: FragmentAdminKelolaMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Menggunakan ViewBinding untuk mengakses elemen UI
        _binding = FragmentAdminKelolaMovieBinding.inflate(inflater, container, false)

        // Menetapkan listener pada tombol addMovie
        binding.addMovie.setOnClickListener {
            // Arahkan ke AdminAddMovie ketika tombol diklik
            val intent = Intent(activity, AdminAddMovieActivity::class.java)
            startActivity(intent)
        }

        // Return view binding root
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val apiService = ApiClient.getInstance()

        // Panggil API untuk mendapatkan semua movie
        apiService.getAllMovies().enqueue(object : retrofit2.Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.isSuccessful && response.body() != null) {
                    // Mengonversi List menjadi MutableList untuk memodifikasi data
                    val movies = response.body()!!.toMutableList() // Mengubah List menjadi MutableList

                    // Mengecek apakah binding dan fragment masih valid
                    if (isAdded && _binding != null) {
                        // Atur adapter RecyclerView
                        val adapter = MovieAdapter(
                            movies,
                            onEditClick = { movie ->
                                val intent = Intent(requireContext(), AdminEditMovieActivity::class.java)
                                intent.putExtra("movie_id", movie.id)
                                intent.putExtra("judul", movie.judul)
                                intent.putExtra("director", movie.director)
                                intent.putExtra("tanggal", movie.tanggal)
                                intent.putExtra("deskripsi", movie.deskripsi)
                                intent.putExtra("foto", movie.foto)
                                startActivity(intent)
                            },
                            onDeleteClick = { movie ->
                                deleteMovie(movie, movies)  // Panggil fungsi deleteMovie
                            }
                        )

                        // Menyambungkan adapter ke RecyclerView
                        binding.cardAdmin.apply {
                            layoutManager = GridLayoutManager(requireContext(), 2) // 2 kolom
                            this.adapter = adapter
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteMovie(movie: Movie, movies: MutableList<Movie>) {
        val apiService = ApiClient.getInstance()

        // Tampilkan Toast sebelum menghapus
        Toast.makeText(requireContext(), "Delete ${movie.judul}", Toast.LENGTH_SHORT).show()

        // Panggil API untuk menghapus movie berdasarkan ID
        apiService.deleteMovie(movie.id!!).enqueue(object : retrofit2.Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    // Menghapus movie dari list dan memberi tahu adapter untuk memperbarui UI
                    val position = movies.indexOf(movie)
                    if (position != -1) {
                        movies.removeAt(position)  // Menghapus item
                        // Mengecek apakah binding dan fragment masih valid
                        if (isAdded && _binding != null) {
                            binding.cardAdmin.adapter?.notifyItemRemoved(position)  // Memberitahu adapter untuk menghapus item di posisi tertentu
                        }
                    }
                    Log.d("AdminHomeFragment", "Movie ${movie.judul} berhasil dihapus.")
                } else {
                    Toast.makeText(requireContext(), "Gagal menghapus movie", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    override fun onResume() {
        super.onResume()
        setupRecyclerView() // Panggil ulang API untuk mendapatkan data terbaru
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // Set binding menjadi null setelah fragment dihancurkan untuk menghindari memory leak
        _binding = null
    }
}