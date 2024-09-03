package hr.ferit.dejanmihic.campspottercompose.data.local

import hr.ferit.dejanmihic.campspottercompose.model.User
import hr.ferit.dejanmihic.campspottercompose.ui.utils.localDateToString
import java.time.LocalDate

object LocalUserDataProvider {
    val defaultUser = User(
        uid = "",
        email = "",
        imageUrl = "",
        imageName = "",
        username = "",
        firstName = "",
        lastName = "",
        birthDate = localDateToString(LocalDate.now()),
        creationDate = localDateToString(LocalDate.now()),
    )
    val removedUser = User(
        uid = "",
        email = "",
        imageUrl = "",
        imageName = "",
        username = "user is removed",
        firstName = "",
        lastName = "",
        birthDate = "",
        creationDate = "",
    )
    fun getUsersData(): MutableList<User>{
        return mutableListOf(
            User(
                uid = "gx0K33TXlUSRZ8zjcj9XGn6tDqq1",
                email = "dejan@gmail.com",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/camp-spotter-compose.appspot.com/o/images%2Fbitmap_20240421_170208.jpg?alt=media&token=7765590f-2237-47a9-a7f2-2c613076814e",
                imageName = "bitmap_20240421_170208.jpg",
                username = "deos",
                firstName = "Dejan",
                lastName = "Mihić",
                birthDate = localDateToString(LocalDate.now()),
                creationDate = localDateToString(LocalDate.now()),
            ),
            User(
                uid = "RfJhfvj1OJXbh8F7TMqlnwm46bD2",
                email = "marko@gmail.com",
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/camp-spotter-compose.appspot.com/o/images%2Fbitmap_20240424_103730.jpg?alt=media&token=89935e09-59d4-4903-96ef-021f1a439d5d",
                username = "maki",
                firstName = "Marko",
                lastName = "Dalić",
                birthDate = localDateToString(LocalDate.now()),
                creationDate = localDateToString(LocalDate.now()),
            )
        )
    }
}