package jp.example.markdowneditor

import jp.example.markdowneditor.editor.FormatCommand
import jp.example.markdowneditor.editor.MarkdownFormatting
import org.junit.Assert.assertEquals
import org.junit.Test

class MarkdownFormattingTest {
    @Test
    fun boldWrapsSelectionAndKeepsItSelected() {
        val result = MarkdownFormatting.apply("hello world", FormatCommand.Bold, 6, 11)

        assertEquals("hello **world**", result.content)
        assertEquals(8, result.selectionStart)
        assertEquals(13, result.selectionEnd)
    }

    @Test
    fun italicInsertsMarkersAroundCursor() {
        val result = MarkdownFormatting.apply("hello", FormatCommand.Italic, 2, 2)

        assertEquals("he__llo", result.content)
        assertEquals(3, result.selectionStart)
        assertEquals(result.selectionStart, result.selectionEnd)
    }

    @Test
    fun paragraphSeparatesSelectedTextFromSurroundingText() {
        val result = MarkdownFormatting.apply("beforeselectedafter", FormatCommand.Paragraph, 6, 14)

        assertEquals("before\n\nselected\n\nafter", result.content)
        assertEquals(result.content.indexOf("after"), result.selectionStart)
        assertEquals(result.selectionStart, result.selectionEnd)
    }

    @Test
    fun tableIsInsertedAtCursor() {
        val result = MarkdownFormatting.apply("beforeafter", FormatCommand.Table, 6, 6)

        assertEquals(
            "before| 列1 | 列2 |\n| --- | --- |\n|     |     |\nafter",
            result.content
        )
        assertEquals(result.content.indexOf("after"), result.selectionStart)
        assertEquals(result.selectionStart, result.selectionEnd)
    }
}
