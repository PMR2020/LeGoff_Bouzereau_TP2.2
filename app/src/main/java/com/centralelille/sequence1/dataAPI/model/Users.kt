package com.centralelille.sequence1.dataAPI.model

data class Users(
    val id: Int,
    var pseudo: String,
    var pass: String,
    var hash: String
)