package com.example.uasppapbnajwan.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.uasppapbnajwan.R
import com.example.uasppapbnajwan.databinding.ActivityAdminAddMovieBinding
import com.example.uasppapbnajwan.model.Movie
import com.example.uasppapbnajwan.network.ApiClient
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class AdminAddMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAddMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan ViewBinding
        binding = ActivityAdminAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        binding.btnAdd.setOnClickListener {
            // Ambil nilai dari form input
            val judul = binding.txtJudul.text.toString()
            val director = binding.txtDirector.text.toString()
            val tanggal = binding.txtTanggal.text.toString()
            val deskripsi = binding.txtDeskripsi.text.toString()
            val foto = binding.txtFoto.text.toString()

            // Panggil fungsi addMovie dengan parameter yang benar
            addMovie(judul, director, tanggal, deskripsi, foto)
        }

        // Menambahkan listener untuk tombol btnBack
        binding.btnBack.setOnClickListener {
            // Pastikan NavHostFragment ditemukan
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
            if (navHostFragment != null) {
                // Menggunakan NavController untuk berpindah ke AdminKelolaMovieFragment
                val navController = navHostFragment.navController
                navController.navigate(R.id.adminKelolaMovieFragment) // Menavigasi ke AdminKelolaMovieFragment
            } else {
                // Jika navHostFragment tidak ditemukan, beri log atau toast untuk debugging
                println("NavHostFragment not found!")
            }
            finish() // Menyelesaikan activity ini
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.txtTanggal.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun addMovie(
        judul: String,
        director: String,
        tanggal: String,
        deskripsi: String,
        foto: String
    ) {
        // Log all input values before sending
        Log.d("MovieUpload", "Judul: $judul, Tipe Data: ${judul.javaClass.simpleName}")
        Log.d("MovieUpload", "Director: $director, Tipe Data: ${director.javaClass.simpleName}")
        Log.d("MovieUpload", "Tanggal: $tanggal, Tipe Data: ${tanggal.javaClass.simpleName}")
        Log.d("MovieUpload", "Deskripsi: $deskripsi, Tipe Data: ${deskripsi.javaClass.simpleName}")
        Log.d("MovieUpload", "Foto: $foto, Tipe Data: ${foto.javaClass.simpleName}")

        val jsonData = Gson().toJsonTree(
            mapOf(
                "judul" to judul,
                "director" to director,
                "tanggal" to tanggal,
                "deskripsi" to deskripsi,
                "foto" to foto
            )
        ).toString()

        val requestBody = jsonData.toRequestBody("application/json".toMediaType())

        ApiClient.getInstance().addMovie(requestBody).enqueue(object : Callback<Movie> {
            override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                if (response.isSuccessful) {
                    // Tangani jika data berhasil dikirimkan
                    Toast.makeText(applicationContext, "Data berhasil dikirim!", Toast.LENGTH_SHORT).show()
                    finish()
                    println("Data berhasil dikirim: ${response.body()}")
                } else {
                    // Tangani jika ada error
                    println("Error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Movie>, t: Throwable) {
                // Tangani jika terjadi kesalahan jaringan atau lainnya
                println("Koneksi gagal: ${t.message}")
            }
        })
    }

}