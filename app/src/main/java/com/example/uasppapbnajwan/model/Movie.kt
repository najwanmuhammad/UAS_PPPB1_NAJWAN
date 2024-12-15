package com.example.uasppapbnajwan.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("_id")
    val id: String,

    @SerializedName("judul")
    val judul: String,

    @SerializedName("director")
    val director: String,

    @SerializedName("tanggal")
    val tanggal: String,

    @SerializedName("deskripsi")
    val deskripsi: String,

    @SerializedName("foto")
    val foto: String // URL gambar
)
