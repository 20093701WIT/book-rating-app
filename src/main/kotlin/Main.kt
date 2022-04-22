import mu.KotlinLogging
import utils.ScannerInput
import java.lang.System.exit

private val logger = KotlinLogging.logger {}

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
            0  -> exitApp()
            else -> System.out.println("Invalid option entered: " + option)
        }
    } while (true)
}

fun addBook(){
    logger.info { "addBook() function invoked" }
}

fun listBooks(){
    logger.info { "listBooks() function invoked" }
}

fun updateBook(){
    logger.info { "updateBook() function invoked" }
}

fun deleteBook(){
    logger.info { "deleteBook() function invoked" }
}

fun exitApp(){
    println("Exiting...bye")
    exit(0)
}