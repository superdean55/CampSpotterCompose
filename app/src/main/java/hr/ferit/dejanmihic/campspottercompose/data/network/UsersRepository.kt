package hr.ferit.dejanmihic.campspottercompose.data.network

import hr.ferit.dejanmihic.campspottercompose.model.User

data class UsersRepositoryState(
    val users: MutableList<User> = mutableListOf()
)
object UsersRepository {

}