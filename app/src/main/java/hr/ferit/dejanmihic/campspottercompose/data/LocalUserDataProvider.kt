package hr.ferit.dejanmihic.campspottercompose.data

import android.net.Uri
import hr.ferit.dejanmihic.campspottercompose.model.EditUserForm
import hr.ferit.dejanmihic.campspottercompose.model.UserFormErrors
import hr.ferit.dejanmihic.campspottercompose.model.User
import java.time.LocalDate

object LocalUserDataProvider {
    val defaultUser = EditUserForm(
        id = 1L,
        username = "deos",
        firstName = "",
        lastName = "",
        birthDate = LocalDate.now(),
        creationDate = LocalDate.now(),
        editUserFormErrors = UserFormErrors()
    )
    fun getUsersData(): MutableList<User>{
        return mutableListOf(
            User(
                id = 1L,
                image = Uri.EMPTY,
                username = "deos",
                firstName = "Dejan",
                lastName = "Mihić",
                birthDate = LocalDate.now(),
                creationDate = LocalDate.now()
            ),
            User(
                id = 2L,
                image = Uri.EMPTY,
                username = "maki",
                firstName = "Marko",
                lastName = "Dalić",
                birthDate = LocalDate.now(),
                creationDate = LocalDate.now()
            )
        )
    }
}