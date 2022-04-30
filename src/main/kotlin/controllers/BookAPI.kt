package controllers

import models.Book
import persistence.Serializer

private var books = ArrayList<Book>()

class BookAPI(serializerType: Serializer) {
    private var serializer: Serializer = serializerType

    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun listAllBooks(): String =
        if (books.isEmpty())
            "No books stored"
        else
            formatListString(books)

    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    // utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun formatListString(notesToFormat: List<Book>): String =
        notesToFormat
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun numberOfRecommendedBooks(): Int = books.count { book: Book -> book.BookIsRecommended }
    fun numberOfNotRecommendedBooks(): Int = books.count { book: Book -> book.BookIsRecommended }

    fun listRecommendedBooks(): String =
        if (numberOfRecommendedBooks() == 0) "No recommended books"
        else formatListString(books.filter { book -> book.BookIsRecommended })

    fun listNotRecommendedBooks(): String =
        if (numberOfNotRecommendedBooks() == 0) "No non recommended books"
        else formatListString(books.filter { book -> !book.BookIsRecommended })

    fun listByHighestPrice() =
        books.sortedByDescending { books -> books.BookPrice } // Descending means high to low
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun listByLowestPrice() =
        books.sortedBy { books -> books.BookPrice }
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun deleteBook(indexToDelete: Int): Book? {
        return if (isValidListIndex(indexToDelete, books)) {
            books.removeAt(indexToDelete)
        } else null
    }

    fun updateBook(indexToUpdate: Int, book: Book?): Boolean {
        val foundBook = findBook(indexToUpdate)
        if ((foundBook != null) && (book != null)) {
            foundBook.BookTitle = book.BookTitle
            foundBook.BookAuthor = book.BookAuthor
            foundBook.BookGenre = book.BookGenre
            foundBook.BookReleaseYear = book.BookReleaseYear
            foundBook.BookLength = book.BookLength
            foundBook.BookPrice = book.BookPrice
            foundBook.BookIsRecommended = book.BookIsRecommended
            return true
        }
        return false
    }

    fun isValidIndex(index: Int): Boolean {
        return isValidListIndex(index, books)
    }

    // XML
    @Throws(Exception::class)
    fun load() {
        books = serializer.read() as ArrayList<Book>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(books)
    }

    // LIST AND COUNT BOOK GENRES

    fun numberOfFictionBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("fiction") }

    fun numberOfScifiBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("sci-fi") }

    fun numberOfNonfictionBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("non fiction") }

    fun numberOfEducationBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("education") }

    fun numberOfChildrenBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("children") }

    fun numberOfDramaBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("drama") }

    fun numberOfMediaBooks(): Int =
        books.count { book: Book -> book.BookGenre.contains("media") }

    fun listBooksByGenre(s: String) =
        books.filter { book -> book.BookGenre.contains(s) }
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun listBooksByTitle(s: String) =
        books.filter { book -> book.BookTitle.contains(s) }
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun clearArray() {
        books.clear()
    }
}
