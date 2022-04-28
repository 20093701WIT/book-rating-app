import controllers.BookAPI
import models.Book
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.ScannerInput
import utils.ScannerInput.readNextDouble
import utils.ScannerInput.readNextInt
import utils.ScannerInput.readNextLine
import java.io.File
import java.lang.System.exit

private val logger = KotlinLogging.logger {}
private val bookAPI = BookAPI(XMLSerializer(File("books.xml")))
private val bookAPIJSON = BookAPI(JSONSerializer(File("books.json")))


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
         > |   9) Search by book title      |
         > ----------------------------------
         > |   20) Load XML file            |
         > |   21) Save XML file            |
         > ----------------------------------
         > |   22) Load JSON file           |
         > |   23) Save JSON file           |
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
            9  -> searchByBookTitle()
            20 -> XMLload()
            21 -> XMLsave()
            22 -> JSONload()
            23 -> JSONsave()
            0  -> exitApp()
            else -> System.out.println("Invalid option entered: " + option)
        }
    } while (true)
}

fun addBook(){
    val bookTitle = readNextLine("Enter book title: ")
    val bookAuthor = readNextLine("Enter book author: ")
    //set to lowercase for search by genre code in BookAPI (ln 98 - ln )
    val bookGenre = readNextLine("Enter book genre: ").lowercase()
    val bookReleaseYear = readNextInt("Enter book release year: ")
    val bookLength = readNextInt("Enter book length: ")
    val bookPrice = readNextDouble("Enter book price (in Euro): ")
    //if you write anything but true, it will default to false.
    //I had a readNextBoolean util imported from scannerInput, but I decided to delete as of now
    //as it would end the programme if true/false wasn't entered.
    val bookRecommendation = readNextLine("Do you recommend the book? (true/false): ").toBoolean()

    val isAdded = bookAPI.add(Book(bookTitle, bookAuthor,bookGenre, bookReleaseYear, bookLength, bookPrice, bookRecommendation ))

    if (isAdded) {
        println("Added Successfully")
    } else {
        println("Add Failed")
    }
}

fun listBooks(){
    if (bookAPI.numberOfBooks() > 0) {
        val option = readNextInt(
            """
                  > --------------------------------
                  > |   1) View ALL books          |
                  > |   2) View fiction books      |
                  > |   3) View sci-fi books       |
                  > |   4) View nonfiction books   |
                  > |   5) View educational books  |
                  > |   6) View children books     |
                  > |   7) View drama books        |
                  > |   8) View media books        |
                  > --------------------------------
         > ==>> """.trimMargin(">"))

        when (option) {
            1 -> listAllBooks()
            2 -> listFictionBooks()
            3 -> listScifiBooks()
            4 -> listNonfictionBooks()
            5 -> listEducationalBooks()
            6 -> listChildrenBooks()
            7 -> listDramaBooks()
            8 -> listMediaBooks()
            else -> println("Invalid option entered: " + option);
        }
    } else {
        println("Option Invalid - No notes stored");
    }
}

fun searchByBookTitle() {
    val search = readNextLine("Enter title of note you want to find: ")
    val results = bookAPI.listBooksByTitle(search)
    if (results.isEmpty()) {
        println("no books with matching title")
    } else {
        println(results)
    }
}

// LISTING BOOK GENRE/RECOMMENDATION

fun listAllBooks() {
    println("There are " + bookAPI.listAllBooks() + " total books.")
}

fun listFictionBooks() {
    println("There are " + bookAPI.numberOfFictionBooks() + " fiction books.")
    println(bookAPI.listBooksByGenre("fiction"))
}

fun listScifiBooks() {
    println("There are " + bookAPI.numberOfScifiBooks() + " Science-fiction books.")
    println(bookAPI.listBooksByGenre("sci-fi"))
}

fun listNonfictionBooks() {
    println("There are " + bookAPI.numberOfNonfictionBooks() + "Non fiction books.")
    println(bookAPI.listBooksByGenre("non fiction"))
}

fun listEducationalBooks() {
    println("There are " + bookAPI.numberOfEducationBooks() + "Educational books.")
    println(bookAPI.listBooksByGenre("education"))
}

fun listChildrenBooks() {
    println("There are " + bookAPI.numberOfChildrenBooks() + "Children books.")
    println(bookAPI.listBooksByGenre("children"))
}

fun listDramaBooks() {
    println("There are " + bookAPI.numberOfDramaBooks() + " Drama books.")
    println(bookAPI.listBooksByGenre("drama"))
}

fun listMediaBooks() {
    println("There are " + bookAPI.numberOfMediaBooks() + " Media books.")
    println(bookAPI.listBooksByGenre("media"))
}


fun listRecommendedBooks(){
    println(bookAPI.listRecommendedBooks())
}

fun listNotRecommendedBooks(){
    println(bookAPI.listNotRecommendedBooks())
}

fun listByHighestPrice() {
    println(bookAPI.listByHighestPrice())
}

fun listByLowestPrice() {
    println(bookAPI.listByLowestPrice())
}

fun updateBook(){
    listBooks()
    if (bookAPI.numberOfBooks() > 0) {
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (bookAPI.isValidIndex(indexToUpdate)) {
            val bookTitle = readNextLine("Enter a title for the book: ")
            val bookAuthor = readNextLine("Enter the author for the book: ")
            val bookGenre = readNextLine("Enter the main genre of the book: ")
            val bookReleaseYear = readNextInt("Enter the release year of the book: ")
            val bookLength = readNextInt("Enter how many pages are in the book: ")
            var bookPrice = readNextDouble("Enter how much the book cost (in euro): ")
            //if you write anything but true, it will default to false.
            //I had a readNextBoolean util imported from scannerInput, but I decided to delete as of now
            //as it would end the programme if true/false wasn't entered.
            val bookRecommendation = readNextLine("Do you recommend the book? (true/false): ").toBoolean()


            if (bookAPI.updateBook(indexToUpdate, Book(bookTitle, bookAuthor, bookGenre, bookReleaseYear, bookLength, bookPrice, bookRecommendation))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }}

fun deleteBook(){
    listBooks()
    //checks to see if any books exist. if they don't then it won't ask to delete
    if (bookAPI.numberOfBooks() > 0) {
        val indexToDelete = readNextInt("Enter the index of the book to delete: ")
        val bookToDelete = bookAPI.deleteBook(indexToDelete)
        if (bookToDelete != null) {
            println("Delete Successful! Deleted book: ${bookToDelete.BookTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

//PERSISTENCE - XML

fun XMLsave() {
    try {
        bookAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun XMLload() {
    try {
        bookAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

// PERSISTENCE - JSON

fun JSONsave() {
    try {
        bookAPIJSON.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun JSONload() {
    try {
        bookAPIJSON.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}