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
import kotlin.test.assertNull
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

    @Nested
    inner class DeleteBooks {

        @Test
        fun `deleting a Book that does not exist, returns null`() {
            emptyBooks!!.clearArray()
            assertNull(emptyBooks!!.deleteBook(0))
            assertNull(populatedBooks!!.deleteBook(-1))
            assertNull(populatedBooks!!.deleteBook(5))
        }

        @Test
        fun `deleting a book that exists delete and returns deleted object`() {
            assertEquals(5, populatedBooks!!.numberOfBooks())
            assertEquals(wimpyKid1, populatedBooks!!.deleteBook(3))
            assertEquals(4, populatedBooks!!.numberOfBooks())
            assertEquals(wonder, populatedBooks!!.deleteBook(0))
            assertEquals(3, populatedBooks!!.numberOfBooks())
        }
    }

    @Nested
    inner class ListBooks {

        @Test
        fun `listAllBooks returns No Books Stored message when ArrayList is empty`() {
            emptyBooks!!.clearArray()
            assertEquals(0, emptyBooks!!.numberOfBooks())
            assertTrue(emptyBooks!!.listAllBooks().lowercase().contains("no books"))
        }

        @Test
        fun `listAllBooks returns Books when ArrayList has books stored`() {
            emptyBooks!!.clearArray()
            //This is a hot-fix to the problem relating to me having to manually flush the array.
            populatedBooks!!.add(wonder!!)
            populatedBooks!!.add(noLongerHuman!!)
            populatedBooks!!.add(rollOfThunder!!)
            populatedBooks!!.add(wimpyKid1!!)
            populatedBooks!!.add(gatsby!!)
            assertEquals(5, populatedBooks!!.numberOfBooks())
            val booksString = populatedBooks!!.listAllBooks().lowercase()
            assertTrue(booksString.contains("wonder"))
            assertTrue(booksString.contains("no longer"))
            assertTrue(booksString.contains("roll of"))
            assertTrue(booksString.contains("diary of a"))
            assertTrue(booksString.contains("the great"))
        }
    }
    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty books.XML file.
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.store()

            // Loading the empty books.xml file into a new object
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the XML loaded books (loadedBooks)
            storingBooks.clearArray()
            loadedBooks.clearArray()
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())

        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 books to the books.XML file.
            val storingBooks = BookAPI(XMLSerializer(File("books.xml")))
            storingBooks.clearArray()
            storingBooks.add(wonder!!)
            storingBooks.add(noLongerHuman!!)
            storingBooks.add(rollOfThunder!!)
            storingBooks.store()

            // Loading books.xml into a different collection
            val loadedBooks = BookAPI(XMLSerializer(File("books.xml")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the XML loaded books (loadedBooks)
            assertEquals(3, storingBooks.numberOfBooks())
            assertEquals(3, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(0), loadedBooks.findBook(0))
            assertEquals(storingBooks.findBook(1), loadedBooks.findBook(1))
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
            storingBooks.clearArray()
            loadedBooks.clearArray()
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty books.json file.

            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.clearArray()
            storingBooks.store()

            // Loading the empty books.json file into a new object
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.clearArray()
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the json loaded books (loadedBooks)
            assertEquals(0, storingBooks.numberOfBooks())
            assertEquals(0, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            storingBooks.clearArray()
            loadedBooks.clearArray()
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 books to the books.json file.
            val storingBooks = BookAPI(JSONSerializer(File("books.json")))
            storingBooks.clearArray()
            storingBooks.add(gatsby!!)
            storingBooks.add(rollOfThunder!!)
            storingBooks.add(wimpyKid1!!)
            storingBooks.store()

            // Loading books.json into a different collection
            val loadedBooks = BookAPI(JSONSerializer(File("books.json")))
            loadedBooks.load()

            // Comparing the source of the books (storingBooks) with the json loaded books (loadedBooks)
            assertEquals(3, storingBooks.numberOfBooks())
            assertEquals(3, loadedBooks.numberOfBooks())
            assertEquals(storingBooks.numberOfBooks(), loadedBooks.numberOfBooks())
            assertEquals(storingBooks.findBook(0), loadedBooks.findBook(0))
            assertEquals(storingBooks.findBook(1), loadedBooks.findBook(1))
            assertEquals(storingBooks.findBook(2), loadedBooks.findBook(2))
            storingBooks.clearArray()
            loadedBooks.clearArray()
        }
    }

}