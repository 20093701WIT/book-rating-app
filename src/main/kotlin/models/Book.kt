package models

data class Book(
    var BookTitle: String,
    var BookAuthor: String,
    var BookGenre: String,
    var BookReleaseYear: Int,
    var BookLength: Int,
    var BookPrice: Double,
    var BookIsRecommended: Boolean
)