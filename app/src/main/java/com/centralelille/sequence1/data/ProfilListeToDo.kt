package com.centralelille.sequence1.data

import java.io.Serializable

data class ProfilListeToDo(val login: String) : Serializable {
    var listesToDo: ArrayList<ListeToDo> = ArrayList<ListeToDo>()

    fun getIdList (titre: String): Int{
        for(liste in this.listesToDo) {
            if (liste.titreListeToDo == titre) {
                //var currentList: ListeToDo = liste(None)('ok frr kestuvafer')('le none est en trop aussi')('bon courage frr')
                return(this.listesToDo.indexOf(liste))
            }
        }
        return(-1)
    }
}