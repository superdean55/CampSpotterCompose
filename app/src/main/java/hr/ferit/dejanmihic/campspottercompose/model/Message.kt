package hr.ferit.dejanmihic.campspottercompose.model

data class Message(
    val id: String? = "",
    val userId: String? = "",
    val message: String? = "",
    val postDate: String? = ""
){
    constructor() : this("","","","")
}
