package hr.ferit.dejanmihic.campspottercompose.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val uid: String? = null,
    val imageUrl: String? = null,
    val imageName: String? = null,
    val username: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val birthDate: String? = null,
    val creationDate: String? = null,
){
    constructor() : this(null,null,null,null,null,null,null,null, null)
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
