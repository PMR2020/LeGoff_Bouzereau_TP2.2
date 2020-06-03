package com.centralelille.sequence1.data

data class ProfilListeToDo (val login:String) {
    var listesToDo: ArrayList<ListeToDo> = ArrayList<ListeToDo>()
}