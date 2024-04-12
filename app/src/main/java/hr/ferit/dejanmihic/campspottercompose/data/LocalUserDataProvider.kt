package hr.ferit.dejanmihic.campspottercompose.data

import hr.ferit.dejanmihic.campspottercompose.model.EditUserForm
import hr.ferit.dejanmihic.campspottercompose.model.EditUserFormErrors
import java.time.LocalDate

object LocalUserDataProvider {
    val defaultUser = EditUserForm(
        id = 1L,
        username = "deos",
        firstName = "",
        lastName = "",
        birthDate = LocalDate.now(),
        creationDate = LocalDate.now(),
        editUserFormErrors = EditUserFormErrors()
    )
}