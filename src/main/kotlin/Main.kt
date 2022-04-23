import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import utils.ScannerInput
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextFloat
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val bookAPI = BookAPI()

fun main(args: Array<String>) {
    runMenu()
}

fun mainMenu() : Int {
    return ScannerInput.readNextInt(""" 
         > ----------------------------------
         > |        BOOK RATING APP         |
         > ----------------------------------
         > | BOOK RATING MENU               |
         > |   1) Add a book                |
         > |   2) List all books            |
         > |   3) Update a book             |
         > |   4) Delete a book             |
         > |   5) List recommended books    |
         > |   6) List not recommended books|
         > |   7) List by highest price     |
         > |   8) List by lowest price      |
         > ----------------------------------
         > |   0) Exit                      |
         > ----------------------------------
         > ==>> """.trimMargin(">"))
}

fun runMenu() {
    do {
        val option = mainMenu()
        when (option) {
            1  -> addBook()
            2  -> listBooks()
            3  -> updateBook()
            4  -> deleteBook()
            5  -> listRecommendedBooks()
            6  -> listNotRecommendedBooks()
            7  -> listByHighestPrice()
            8  -> listByLowestPrice()
            0  -> exitApp()
            else -> System.out.println("Invalid option entered: " + option)
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter book title: ")
    val bookAuthor = readNextLine("Enter book author: ")
    val bookGenre = readNextLine("Enter book genre: ")
    val bookReleaseYear = readNextInt("Enter book release year: ")
    val bookLength = readNextInt("Enter book length: ")
    val bookPrice = readNextDouble("Enter book price (in Euro): ")
    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor,bookGenre, bookReleaseYear, bookLength, bookPrice, false ))
    //val bookIsRecommended

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    println(bookAPI.listAllBooks())
}

fun listRecommendedBooks(){
    println(bookAPI.listRecommendedBooks())
}

fun listNotRecommendedBooks(){
    println(bookAPI.listNotRecommendedBooks())
}

fun updateBook(){
    logger.info { "updateBook() function invoked" }
}

fun deleteBook(){
    logger.info { "deleteBook() function invoked" }
}

fun listByHighestPrice() {
    println(bookAPI.listByHighestPrice())
}

fun listByLowestPrice() {
    println(bookAPI.listByLowestPrice())
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}