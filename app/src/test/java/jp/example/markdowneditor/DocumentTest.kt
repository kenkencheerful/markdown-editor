package jp.example.markdowneditor

import jp.example.markdowneditor.data.Document
import org.junit.Assert.assertEquals
import org.junit.Test

class DocumentTest {
    @Test
    fun copyWithUpdatesTitleAndTimestamp() {
        val document = Document(title = "Before", content = "Content", updatedAt = 1L)
        val copied = document.copyWith(title = "After")

        assertEquals("After", copied.title)
        assertEquals("Content", copied.content)
        assertEquals(document.id, copied.id)
        assertEquals(document.createdAt, copied.createdAt)
    }

    @Test
    fun copyWithUpdatesContentWithoutChangingTitle() {
        val document = Document(title = "Title", content = "Before")
        val copied = document.copyWith(content = "After")

        assertEquals("Title", copied.title)
        assertEquals("After", copied.content)
    }
}
