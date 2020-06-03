package com.centralelille.sequence1.data

data class ListeToDo(var titreListeToDo: String) {
    var itemsToDo: ArrayList<ItemToDo> = ArrayList<ItemToDo>()

}