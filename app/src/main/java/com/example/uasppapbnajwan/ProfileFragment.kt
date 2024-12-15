package com.example.uasppapbnajwan

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.uasppapbnajwan.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi SharedPreferences untuk mendapatkan data pengguna
        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout dengan ViewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Ambil informasi dari SharedPreferences atau sumber data lainnya
        val username = sharedPreferences.getString("USERNAME", "Unknown User") ?: "Unknown User"
        val phone = sharedPreferences.getString("PHONE", "Unknown Phone") ?: "Unknown Phone"
        val email = sharedPreferences.getString("EMAIL", "Unknown Email") ?: "Unknown Email"

        // Set data ke dalam UI menggunakan ViewBinding
        binding.username.text = username
        binding.email.text = email

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}
