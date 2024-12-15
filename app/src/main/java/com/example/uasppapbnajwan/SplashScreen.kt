package com.example.uasppapbnajwan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.uasppapbnajwan.admin.AdminActivity
import com.example.uasppapbnajwan.databinding.SplashScreenBinding
import com.example.uasppapbnajwan.user.UserActivity

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: SplashScreenBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup View Binding
        binding = SplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        // Handle click on welcomeBtn
        binding.welcomeBtn.setOnClickListener {
            // Cek status login
            if (getLoginStatus()) {
                val email = getStoredEmail()  // Mendapatkan email yang tersimpan
                navigateToUserOrAdmin(email)  // Menavigasi ke halaman user atau admin
                finishAffinity()  // Menutup SplashScreen dan activity sebelumnya
            } else {
                // Jika belum login, intent ke MainActivity
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Close SplashScreen to prevent going back to it
            }
        }
    }

    // Mendapatkan status login
    private fun getLoginStatus(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    // Mendapatkan email yang sudah disimpan
    private fun getStoredEmail(): String {
        return sharedPreferences.getString("userEmail", "") ?: ""
    }

    // Menavigasi ke halaman berdasarkan role
    private fun navigateToUserOrAdmin(email: String) {
        val userType = getUserTypeFromEmail(email)

        val intent = if (userType == "admin") {
            // Mengarahkan ke halaman admin
            Intent(this@SplashScreen, AdminActivity::class.java)
        } else {
            // Mengarahkan ke halaman user
            Intent(this@SplashScreen, UserActivity::class.java)
        }

        startActivity(intent)
    }

    // Mengambil user type berdasarkan email
    private fun getUserTypeFromEmail(email: String): String {
        return if (email.contains("admin")) {
            "admin"
        } else {
            "user"
        }
    }

}