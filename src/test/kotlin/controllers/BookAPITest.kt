package controllers

import models.Book
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


class BookAPITest {

    private var wonder: Book? = null
    private var noLongerHuman: Book? = null
    private var rollOfThunder: Book? = null
    private var wimpyKid1: Book? = null
    private var gatsby: Book? = null
    private var populatedBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))
    private var emptyBooks: BookAPI? = BookAPI(XMLSerializer(File("books.xml")))

    @BeforeEach
    fun setup() {
        wonder = Book("Wonder", "R. J. Palacio", "fiction", 2012, 315, 9.99, true)
        noLongerHuman = Book("No Longer Human", "Osamu Dazai", "drama", 1948, 170, 7.99, true)
        rollOfThunder = Book("Roll Of Thunder, Hear My Cry", "Mildred D. Taylor", "fiction", 1976, 220, 8.99, true)
        wimpyKid1 = Book("Diary of a Wimpy Kid", "Jeff Kinney", "children", 2007, 217, 6.99, false)
        gatsby = Book("The Great Gatsby", "Scott F. Fitzgerald", "drama", 1925, 176, 6.99, true)

        // adding 5 book to the books api
        populatedBooks!!.add(wonder!!)
        populatedBooks!!.add(noLongerHuman!!)
        populatedBooks!!.add(rollOfThunder!!)
        populatedBooks!!.add(wimpyKid1!!)
        populatedBooks!!.add(gatsby!!)
    }

    @AfterEach
    fun tearDown() {
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
            emptyBooks!!.clearArray()
            //Manually flushing as objects would carry over from previous tests.

            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.add(newBook))
            assertEquals(1, emptyBooks!!.numberOfBooks())
            assertEquals(newBook, emptyBooks!!.findBook(emptyBooks!!.numberOfBooks() - 1))
        }
    }

    @Nested
    inner class UpdateBooks {
        @Test
        fun `updating a book that does not exist returns false`() {
            emptyBooks!!.clearArray()
            populatedBooks!!.clearArray()
            assertFalse(populatedBooks!!.updateBook(6, Book("Wonder", "R. J. Palacio", "fiction", 2012, 315, 9.99, false)))
            assertFalse(populatedBooks!!.updateBook(-1, Book("Wonder", "R. J. Palacio", "fiction", 2012, 315, 9.99, false)))
            assertFalse(emptyBooks!!.updateBook(0, Book("Wonder", "R. J. Palacio", "fiction", 2012, 315, 9.99, false)))
        }

        @Test
        fun `updating a book that exists returns true and updates`() {
            // check book 4 exists and check the contents
            assertEquals(wimpyKid1, populatedBooks!!.findBook(4))
            assertEquals("Diary of a Wimpy Kid", populatedBooks!!.findBook(4)!!.BookTitle)
            assertEquals("Jeff Kinney", populatedBooks!!.findBook(4)!!.BookAuthor)
            assertEquals("children", populatedBooks!!.findBook(4)!!.BookGenre)
            assertEquals(2007, populatedBooks!!.findBook(4)!!.BookReleaseYear)
            assertEquals(217, populatedBooks!!.findBook(4)!!.BookLength)
            assertEquals(6.99, populatedBooks!!.findBook(4)!!.BookPrice)

            // update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedBooks!!.updateBook(4, Book("Journal of a Simpy Kid", "Joe Kenny", "media", 2011, 327, 9.99, false)))
            assertEquals("Journal of a Simpy Kid", populatedBooks!!.findBook(4)!!.BookTitle)
            assertEquals("Joe Kenny", populatedBooks!!.findBook(4)!!.BookAuthor)
            assertEquals("media", populatedBooks!!.findBook(4)!!.BookGenre)
            assertEquals(2011, populatedBooks!!.findBook(4)!!.BookReleaseYear)
            assertEquals(327, populatedBooks!!.findBook(4)!!.BookLength)
            assertEquals(9.99, populatedBooks!!.findBook(4)!!.BookPrice)
        }
    }

}