package utils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import utils.CategoryUtility.categories
import utils.CategoryUtility.isValidCategory

internal class CategoryUtilityTest {
    @Test
    fun categoriesReturnsFullCategoriesSet() {
        Assertions.assertEquals(7, categories.size)
        Assertions.assertTrue(categories.contains("fiction"))
        Assertions.assertTrue(categories.contains("sci-fi"))
        Assertions.assertTrue(categories.contains("nonfiction"))
        Assertions.assertTrue(categories.contains("educational"))
        Assertions.assertTrue(categories.contains("children"))
        Assertions.assertTrue(categories.contains("drama"))
        Assertions.assertTrue(categories.contains("media"))
        Assertions.assertFalse(categories.contains(""))
    }

    @Test
    fun isValidCategoryTrueWhenCategoryExists() {
        Assertions.assertTrue(isValidCategory("fiction"))
        Assertions.assertTrue(isValidCategory("FiCtIoN"))
        Assertions.assertTrue(isValidCategory("FICTION"))
    }

    @Test
    fun isValidCategoryFalseWhenCategoryDoesNotExist() {
        Assertions.assertFalse(isValidCategory("vbasid"))
        Assertions.assertFalse(isValidCategory("sci-fin"))
        Assertions.assertFalse(isValidCategory(""))
    }
}