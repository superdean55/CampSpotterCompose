package hr.ferit.dejanmihic.campspottercompose.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String? = "",
    val imageUrl: String? = "",
    val imageName: String? = "",
    val username: String? = "",
    val firstName: String? = "",
    val lastName: String? = "",
    val email: String? = "",
    val birthDate: String? = "",
    val creationDate: String? = "",
){
    constructor() : this("","","","","","","","", "")
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "imageUrl" to imageUrl,
            "imageName" to imageName,
            "username" to username,
            "firstName" to firstName,
            "lastName" to lastName,
            "email" to email,
            "birthDate" to birthDate,
            "creationDate" to creationDate
        )
    }
}
