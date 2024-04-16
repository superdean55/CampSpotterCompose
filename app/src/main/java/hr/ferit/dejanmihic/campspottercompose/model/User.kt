package hr.ferit.dejanmihic.campspottercompose.model


import android.net.Uri
import androidx.annotation.DrawableRes
import java.time.LocalDate

data class User(
    var id: Long,
    var image: Uri,
    var username: String,
    var firstName: String,
    var lastName: String,
    var birthDate: LocalDate,
    var creationDate: LocalDate,
)
