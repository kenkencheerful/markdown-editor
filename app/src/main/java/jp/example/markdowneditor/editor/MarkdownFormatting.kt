package jp.example.markdowneditor.editor

data class FormattingResult(
    val content: String,
    val selectionStart: Int,
    val selectionEnd: Int
)

enum class FormatCommand {
    Bold,
    Italic,
    Paragraph,
    Table
}

object MarkdownFormatting {
    fun apply(
        content: String,
        command: FormatCommand,
        selectionStart: Int,
        selectionEnd: Int
    ): FormattingResult {
        val start = minOf(selectionStart, selectionEnd).coerceIn(0, content.length)
        val end = maxOf(selectionStart, selectionEnd).coerceIn(0, content.length)
        val selectedText = content.substring(start, end)

        return when (command) {
            FormatCommand.Bold -> wrap(content, start, end, selectedText, "**")
            FormatCommand.Italic -> wrap(content, start, end, selectedText, "_")
            FormatCommand.Paragraph -> separateParagraph(content, start, end, selectedText)
            FormatCommand.Table -> {
                val table = "| 列1 | 列2 |\n| --- | --- |\n|     |     |\n"
                replace(content, start, end, table, start + table.length)
            }
        }
    }

    private fun wrap(
        content: String,
        start: Int,
        end: Int,
        selectedText: String,
        marker: String
    ): FormattingResult {
        val replacement = "$marker$selectedText$marker"
        val selectionStart = start + marker.length
        val selectionEnd = selectionStart + selectedText.length
        return replace(content, start, end, replacement, selectionStart, selectionEnd)
    }

    private fun separateParagraph(
        content: String,
        start: Int,
        end: Int,
        selectedText: String
    ): FormattingResult {
        val prefix = if (start > 0 && content[start - 1] != '\n') "\n\n" else ""
        val suffix = if (end < content.length && content[end] != '\n') "\n\n" else ""
        val replacement = prefix + selectedText + suffix
        val cursor = start + replacement.length
        return replace(content, start, end, replacement, cursor)
    }

    private fun replace(
        content: String,
        start: Int,
        end: Int,
        replacement: String,
        selectionStart: Int,
        selectionEnd: Int = selectionStart
    ) = FormattingResult(
        content = content.replaceRange(start, end, replacement),
        selectionStart = selectionStart,
        selectionEnd = selectionEnd
    )
}
