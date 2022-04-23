package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BookAPITest {

    private var wonder: Book? = null
    private var noLongerHuman: Book? = null
    private var rollOfThunder: Book? = null
    private var wimpyKid1: Book? = null
    private var gatsby: Book? = null
    private var populatedBooks: BookAPI? = BookAPI()
    private var emptyBooks: BookAPI? = BookAPI()

    @BeforeEach
    fun setup(){
        wonder = Book("Wonder", "R. J. Palacio","Ficton", 2012, 315, 9.99, true)
        noLongerHuman = Book("No Longer Human", "Osamu Dazai", "Fiction", 1948, 170, 7.99, true)
        rollOfThunder = Book("Roll Of Thunder, Hear My Cry", "Mildred D. Taylor", "Fiction", 1976, 220, 8.99, true)
        wimpyKid1 = Book("Diary of a Wimpy Kid", "Jeff Kinney", "Fiction", 2007, 217, 6.99, false)
        gatsby = Book("The Great Gatsby", "Scott F. Fitzgerald", "Fiction", 1925, 176, 6.99, true)

        //adding 5 Book to the books api
        populatedBooks!!.add(wonder!!)
        populatedBooks!!.add(noLongerHuman!!)
        populatedBooks!!.add(rollOfThunder!!)
        populatedBooks!!.add(wimpyKid1!!)
        populatedBooks!!.add(gatsby!!)
    }

    @AfterEach
    fun tearDown(){
        wonder = null
        noLongerHuman = null
        rollOfThunder = null
        wimpyKid1 = null
        gatsby = null
        populatedBooks = null
        emptyBooks = null
    }

    @Nested
    inner class AddBooks {
        @Test
        fun `adding a Book to a populated list adds to ArrayList`() {
            val newBook = Book("test", "author", "science", 2002, 173, 4.99, false)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertTrue(populatedBooks!!.add(newBook))
            assertEquals(6, populatedBooks!!.numberOfBooks())
            assertEquals(newBook, populatedBooks!!.findBook(populatedBooks!!.numberOfBooks() - 1))
        }

        @Test
        fun `adding a Book to an empty list adds to ArrayList`() {
            val newBook = Book("test", "author", "science", 2002, 173, 4.99, false)
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class ListBooks {

        @Test
        fun `listAllBooks returns No Books Stored message when ArrayList is empty`() {
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books stored"))
        }

        @Test
        fun `listAllBooks returns Books when ArrayList has books stored`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val booksString = populatedBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("wonder"))
            assertTrue(booksString.contains("no"))
            assertTrue(booksString.contains("roll"))
            assertTrue(booksString.contains("diary"))
            assertTrue(booksString.contains("the"))
        }
    }
}