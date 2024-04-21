package hr.ferit.dejanmihic.campspottercompose.data.local

import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.utils.localDateToString
import java.time.LocalDate

object LocalUserDataProvider {
    val defaultUser = User(
        uid = "",
        email = "",
        imageUrl = "",
        username = "",
        firstName = "",
        lastName = "",
        birthDate = localDateToString(LocalDate.now()),
        creationDate = localDateToString(LocalDate.now()),
    )
    fun getUsersData(): MutableList<User>{
        return mutableListOf(
            User(
                uid = "1",
                email = "dejan@gmail.com",
                imageUrl = "",
                username = "deos",
                firstName = "Dejan",
                lastName = "Mihić",
                birthDate = localDateToString(LocalDate.now()),
                creationDate = localDateToString(LocalDate.now()),
            ),
            User(
                uid = "2",
                email = "marko@gmail.com",
                imageUrl = "",
                username = "maki",
                firstName = "Marko",
                lastName = "Dalić",
                birthDate = localDateToString(LocalDate.now()),
                creationDate = localDateToString(LocalDate.now()),
            )
        )
    }
}