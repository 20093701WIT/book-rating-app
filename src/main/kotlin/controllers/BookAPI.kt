package controllers

import models.Book

private var books = ArrayList<Book>()

class BookAPI {
    fun add(book: Book): Boolean {
        return books.add(book)
    }

    fun listAllBooks(): String {
        return if (books.isEmpty()) {
            "No Books stored"
        } else {
            var listOfBooks = ""
            for (i in books.indices) {
                listOfBooks += "${i}: ${books[i]} \n"
            }
            listOfBooks
        }
    }

    fun numberOfBooks(): Int {
        return books.size
    }

    fun findBook(index: Int): Book? {
        return if (isValidListIndex(index, books)) {
            books[index]
        } else null
    }

    //utility method to determine if an index is valid in a list.
    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }

    fun listRecommendedBooks(): String {
        return if (numberOfBooks() == 0) {
            "No books stored"
        } else {
            var listOfRecommendedBooks = ""
            for (book in books) {
                if (book.BookIsRecommended) {
                    listOfRecommendedBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfRecommendedBooks
        }
    }

    fun listNotRecommendedBooks(): String {
        return if (numberOfBooks() == 0) {
            "No books stored"
        } else {
            var listOfNotRecommendedBooks = ""
            for (book in books) {
                if (!book.BookIsRecommended) {
                    listOfNotRecommendedBooks += "${books.indexOf(book)}: $book \n"
                }
            }
            listOfNotRecommendedBooks
        }
    }

    fun listByHighestPrice() =
        books.sortedByDescending { books -> books.BookPrice } //Descending means high to low
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

    fun listByLowestPrice() =
        books.sortedBy { books -> books.BookPrice }
            .joinToString { book -> books.indexOf(book).toString() + ": " + book.toString() + "\n" }

}