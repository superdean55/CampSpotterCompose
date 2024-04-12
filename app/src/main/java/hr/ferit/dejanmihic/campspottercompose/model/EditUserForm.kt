package hr.ferit.dejanmihic.campspottercompose.model

import android.net.Uri
import java.time.LocalDate

data class EditUserForm(
    val id: Long,
    val imageUri: Uri? = Uri.EMPTY,
    val username: String,
    val firstName: String,
    val lastName: String,
    val birthDate: LocalDate,
    val creationDate: LocalDate,
    val editUserFormErrors: EditUserFormErrors,
)
