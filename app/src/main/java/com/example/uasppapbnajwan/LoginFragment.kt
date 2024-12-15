package com.example.uasppapbnajwan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uasppapbnajwan.admin.AdminActivity
import com.example.uasppapbnajwan.databinding.FragmentLoginBinding
import com.example.uasppapbnajwan.user.UserActivity


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        with(binding) {
            // Memeriksa apakah pengguna sudah login sebelumnya
            if (getLoginStatus()) {
                val email = getStoredEmail()
                navigateToUserOrAdmin(email)
                activity?.finishAffinity()
            }

            // Mengatur tombol login
            login.setOnClickListener {
                val email = txtEmail.text.toString()
                val password = txtPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    // Mendapatkan email dan password yang disimpan dari SharedPreferences
                    val storedEmail = sharedPreferences.getString("EMAIL", "")
                    val storedPassword = sharedPreferences.getString("PASSWORD", "")

                    // Memeriksa apakah email dan password yang dimasukkan cocok
                    if (email == storedEmail && password == storedPassword) {
                        // Menyimpan status login
                        saveLoginStatus(true, email)

                        // Menavigasi ke halaman berdasarkan role
                        navigateToUserOrAdmin(email)
                    } else {
                        Toast.makeText(requireContext(), "Email atau password salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Menyimpan status login dan email
    private fun saveLoginStatus(isLoggedIn: Boolean, email: String) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.putString("userEmail", email)
        editor.apply()
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
            Intent(requireContext(), AdminActivity::class.java)
        } else {
            // Mengarahkan ke halaman user
            Intent(requireContext(), UserActivity::class.java)
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