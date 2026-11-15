package jp.example.markdowneditor.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringValue
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.flow.flow

private const val DATASTORE_NAME = "markdown_editor_datastore"
private const val KEY_DOCUMENT = "document_content"

class DocumentRepository private constructor(
    private val context: Context,
    private val json: Json = Json
) {

    private val dataStore = context.preferencesDataStore(name = DATASTORE_NAME)

    companion object {
        @Volatile
        private var instance: DocumentRepository? = null

        fun getInstance(context: Context): DocumentRepository {
            return instance ?: synchronized(this) {
                instance ?: DocumentRepository(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveDocument(document: Document) {
        val jsonString = json.encodeToString(Document.serializer(), document)
        dataStore.edit { preferences ->
            preferences[stringValue(KEY_DOCUMENT)] = jsonString
        }
    }

    fun loadDocument(): Flow<Document?> = dataStore.data.map { preferences ->
        val jsonString = preferences[stringValue(KEY_DOCUMENT)] ?: return@map null
        try {
            json.decodeFromString(Document.serializer(), jsonString)
        } catch (e: Exception) {
            null
        }
    }

    fun loadDocumentSuspend(): Document? {
        return runBlocking {
            dataStore.data.firstOrNull { preferences ->
                preferences[stringValue(KEY_DOCUMENT)] != null
            }?.let { preferences ->
                val jsonString = preferences[stringValue(KEY_DOCUMENT)]!!
                try {
                    json.decodeFromString(Document.serializer(), jsonString)
                } catch (e: Exception) {
                    null
                }
            }
        }
    }

    fun deleteDocument() {
        dataStore.edit { preferences ->
            preferences[stringValue(KEY_DOCUMENT)] = null
        }
    }
}
