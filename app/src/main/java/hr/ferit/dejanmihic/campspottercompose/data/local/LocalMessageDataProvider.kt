package hr.ferit.dejanmihic.campspottercompose.data.local

import hr.ferit.dejanmihic.campspottercompose.model.Message

object LocalMessageDataProvider {
    fun getMessages(): MutableList<Message>{
        return mutableListOf(
            Message(
                id = "1",
                userId = "gx0K33TXlUSRZ8zjcj9XGn6tDqq1",
                message = "This ist first message from user one",
                postDate = "01.01.2024. 10:00"
            ),
            Message(
                id = "2",
                userId = "gx0K33TXlUSRZ8zjcj9XGn6tDqq1",
                message = "This ist second message from user one",
                postDate = "01.01.2024. 11:00"
            ),
            Message(
                id = "3",
                userId = "RfJhfvj1OJXbh8F7TMqlnwm46bD2",
                message = "user two",
                postDate = "02.01.2024. 14:00"
            ),
            Message(
                id = "4",
                userId = "gx0K33TXlUSRZ8zjcj9XGn6tDqq1",
                message = "This ist user one",
                postDate = "02.01.2024. 14:22"
            ),
            Message(
                id = "5",
                userId = "RfJhfvj1OJXbh8F7TMqlnwm46bD2",
                message = "second time user two",
                postDate = "03.01.2024. 08:00"
            )
        )
    }
}