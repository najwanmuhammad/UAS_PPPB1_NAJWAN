package com.example.uasppapbnajwan.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "favorite_movies")
data class MovieEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(), // Ubah tipe data ke String
    val judul: String,
    val director: String,
    val tanggal: String,
    val deskripsi: String,
    val foto: String,
)
