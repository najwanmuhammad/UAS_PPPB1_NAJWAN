package com.example.uasppapbnajwan

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.uasppapbnajwan.database.AppDatabase
import com.example.uasppapbnajwan.database.MovieEntity
import com.example.uasppapbnajwan.databinding.FragmentUserHomeBinding
import com.example.uasppapbnajwan.model.Movie
import com.example.uasppapbnajwan.network.ApiClient
import com.example.uasppapbnajwan.user.UserDetailMovieActivity
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserHomeFragment : Fragment() {
    private var _binding: FragmentUserHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fungsi logout pada btnBack
        binding.btnBack.setOnClickListener {
            logoutUser()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val db = AppDatabase.getDatabase(requireContext())
        val movieDao = db.movieDao()
        val apiService = ApiClient.getInstance()

        // Panggil API untuk mendapatkan semua movie
        apiService.getAllMovies().enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (!isAdded || _binding == null) return // Fragment tidak aktif, batalkan operasi

                if (response.isSuccessful && response.body() != null) {
                    val movies = response.body()!!

                    // Atur adapter RecyclerView
                    val adapter = UserMovieAdapter(
                        movies,
                        onloveClick = { movie ->
                            lifecycleScope.launch {
                                val movieEntity = MovieEntity(
                                    id = movie.id!!,
                                    judul = movie.judul,
                                    director = movie.director,
                                    tanggal = movie.tanggal,
                                    deskripsi = movie.deskripsi,
                                    foto = movie.foto
                                )
                                movieDao.insertMovie(movieEntity)
                                if (isAdded && _binding != null) {
                                    Toast.makeText(
                                        requireContext(),
                                        "${movie.judul} ditambahkan ke favorit!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    )

                    adapter.setOnItemClickListener { movie ->
                        if (isAdded && _binding != null) {
                            val intent = Intent(requireContext(), UserDetailMovieActivity::class.java)
                            intent.putExtra("judul", movie.judul)
                            intent.putExtra("director", movie.director)
                            intent.putExtra("tanggal", movie.tanggal)
                            intent.putExtra("deskripsi", movie.deskripsi)
                            intent.putExtra("foto", movie.foto)
                            startActivity(intent)
                        }
                    }

                    binding.cardUser.apply {
                        layoutManager = GridLayoutManager(requireContext(), 2)
                        this.adapter = adapter
                    }
                } else {
                    if (isAdded && _binding != null) {
                        Toast.makeText(requireContext(), "Gagal mengambil data!", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                if (isAdded && _binding != null) {
                    Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        })
    }

    private fun logoutUser() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.putString("userEmail", null)
        editor.apply()

        val intent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(intent)
        activity?.finishAffinity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}