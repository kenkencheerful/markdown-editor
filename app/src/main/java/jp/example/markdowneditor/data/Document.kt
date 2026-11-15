package jp.example.markdowneditor.data

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String = "",
    val content: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun copyWith(
        title: String? = null,
        content: String? = null
    ): Document = copy(
        title = title ?: this.title,
        content = content ?: this.content,
        updatedAt = System.currentTimeMillis()
    )
}
