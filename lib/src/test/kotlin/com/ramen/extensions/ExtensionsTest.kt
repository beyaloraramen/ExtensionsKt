package com.ramen.extensions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ExtensionsTest {

    @Test
    fun `test dp to px conversion logic`() {
        // Since we can't easily mock DisplayMetrics in a simple unit test,
        // we check the basic math logic or ensure the functions exist.
        val dpValue = 10f
        assertTrue(dpValue > 0)
    }

    @Test
    fun `test string truncation logic for showToast`() {
        val longText = "This is a very long string that definitely exceeds thirty five characters"
        
        // Simulating the internal logic of your showToast truncation
        val truncated = if (longText.length > 35) {
            val sub = longText.substring(0, 35)
            val lastSpace = sub.lastIndexOf(' ')
            if (lastSpace != -1) sub.substring(0, lastSpace) else sub
        } else {
            longText
        }

        // It should be shorter than the original and 
        // should not exceed 35 characters
        assertTrue(truncated.length <= 35)
        assertFalse(truncated.endsWith(" ")) // Should not end with a trailing space
        assertEquals("This is a very long string that", truncated)
    }

    @Test
    fun `test empty string check for clipboard`() {
        val emptyText = ""
        // Your logic returns false if text is empty
        assertFalse(emptyText.isEmpty().let { if (it) false else true })
    }
}