package models

data class Book(
    val BookTitle: String,
    val BookAuthor: String,
    val BookGenre: String,
    val BookReleaseYear:Int,
    val BookLength: Int,
    val BookPrice: Float,
    val BookIsRecommended: Boolean
     ){
}