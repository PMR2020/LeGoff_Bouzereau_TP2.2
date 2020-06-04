package com.centralelille.sequence1.data

import java.io.Serializable

data class ProfilListeToDo(val login: String) : Serializable {
    var listesToDo: ArrayList<ListeToDo> = ArrayList<ListeToDo>()
}