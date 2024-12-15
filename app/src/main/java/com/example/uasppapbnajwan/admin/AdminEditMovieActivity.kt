package com.example.uasppapbnajwan.admin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.uasppapbnajwan.R
import com.example.uasppapbnajwan.databinding.ActivityAdminEditMovieBinding
import com.example.uasppapbnajwan.model.Movie
import com.example.uasppapbnajwan.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminEditMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminEditMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding
        binding = ActivityAdminEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val movieId = intent.getStringExtra("movie_id")
        val judul = intent.getStringExtra("judul")
        val director = intent.getStringExtra("director")
        val tanggal = intent.getStringExtra("tanggal")
        val deskripsi = intent.getStringExtra("deskripsi")
        val foto = intent.getStringExtra("foto")

        // Set data ke EditText
        binding.txtJudul.setText(judul)
        binding.txtDirector.setText(director)
        binding.txtTanggal.setText(tanggal)
        binding.txtDeskripsi.setText(deskripsi)
        binding.txtFoto.setText(foto)

        // Menambahkan listener untuk tombol btnBack
        binding.btnBack.setOnClickListener {
            // Menavigasi kembali ke AdminKelolaMovieFragment
            onBackPressed()
        }

        // Menambahkan listener untuk tombol btnSave
        binding.btnEdit.setOnClickListener {
            // Ambil data dari EditText
            val updatedJudul = binding.txtJudul.text.toString()
            val updatedDirector = binding.txtDirector.text.toString()
            val updatedTanggal = binding.txtTanggal.text.toString()
            val updatedDeskripsi = binding.txtDeskripsi.text.toString()
            val updatedFoto = binding.txtFoto.text.toString()

            // Pastikan movieId tidak null
            if (movieId != null) {
                // Membuat objek Movie dengan data baru
                val updatedMovie = Movie(
                    id = movieId,
                    judul = updatedJudul,
                    director = updatedDirector,
                    tanggal = updatedTanggal,
                    deskripsi = updatedDeskripsi,
                    foto = updatedFoto
                )
                Log.d("API Request", "Updating Movie with ID: $movieId")
                Log.d("Updated Movie", "ID: ${updatedMovie.id}, Judul: ${updatedMovie.judul}, Director: ${updatedMovie.director}, Tanggal: ${updatedMovie.tanggal}")


                // Panggil API untuk update movie
                val apiService = ApiClient.getInstance()
                apiService.updateMovie(movieId, updatedMovie).enqueue(object : Callback<Movie> {
                    override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                        if (response.isSuccessful) {
                            // Tampilkan pesan sukses
                            Toast.makeText(this@AdminEditMovieActivity, "Movie berhasil diupdate", Toast.LENGTH_SHORT).show()
                            finish()  // Kembali ke halaman sebelumnya setelah sukses
                        } else {
                            // Tampilkan pesan error
                            Toast.makeText(this@AdminEditMovieActivity, "Gagal mengupdate Movie", Toast.LENGTH_SHORT).show()
                            Log.e("API Error", "Response Code: ${response.code()}, Message: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<Movie>, t: Throwable) {
                        // Tampilkan pesan error
                        Toast.makeText(this@AdminEditMovieActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this@AdminEditMovieActivity, "Movie ID tidak valid", Toast.LENGTH_SHORT).show()
            }
        }
    }
}