package com.centralelille.sequence1.data

import java.io.Serializable

data class ListeToDo(var titreListeToDo: String): Serializable {
    var itemsToDo: ArrayList<ItemToDo> = ArrayList<ItemToDo>()

}