package com.example.uasppapbnajwan.user

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.uasppapbnajwan.R
import com.example.uasppapbnajwan.databinding.ActivityUserDetailMovieBinding

class UserDetailMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil data dari Intent
        val judul = intent.getStringExtra("judul")
        val director = intent.getStringExtra("director")
        val tanggal = intent.getStringExtra("tanggal")
        val deskripsi = intent.getStringExtra("deskripsi")
        val foto = intent.getStringExtra("foto")

        // Tampilkan data ke tampilan menggunakan ViewBinding
        binding.txtTitle.text = judul
        binding.txtDirector.text = director
        binding.txtDate.text = tanggal
        binding.txtDesc.text = deskripsi

        // Gunakan Glide atau library lain untuk menampilkan gambar
        Glide.with(this).load(foto).into(binding.imgId)

        // Menangani tombol kembali
        binding.btnBack.setOnClickListener {
            onBackPressed() // Kembali ke activity sebelumnya
        }

    }
}