package hr.ferit.dejanmihic.campspottercompose.model

data class CampSpotFormErrors(
    val isTitleError: Boolean = false,
    val isDescriptionError: Boolean = false,
    val isNumberOfPeopleError: Boolean = false,
    val isLocationDetailsError: Boolean = false,
    val isImageUriError: Boolean = false
)
