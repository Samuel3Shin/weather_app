package com.example.weather

import android.app.SearchManager
import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.provider.BaseColumns
import android.util.Log


class PlacesSuggestionProvider : ContentProvider() {
    companion object {
        private const val LOG_TAG = "ExampleApp"
        const val AUTHORITY = "com.example.google.places.search_suggestion_provider"
        val CONTENT_URI: Uri = Uri.parse("content://" + AUTHORITY + "/search")

        // UriMatcher constant for search suggestions
        private const val SEARCH_SUGGEST = 1
        private var uriMatcher: UriMatcher? = null
        private val SEARCH_SUGGEST_COLUMNS = arrayOf(
            BaseColumns._ID,
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        )

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST)
            uriMatcher!!.addURI(
                AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY + "/*",
                SEARCH_SUGGEST
            )
        }
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher!!.match(uri)) {
            SEARCH_SUGGEST -> SearchManager.SUGGEST_MIME_TYPE
            else -> throw IllegalArgumentException("Unknown URL $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        TODO("Not yet implemented")
        throw UnsupportedOperationException()
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String?>?, selection: String?, selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor {
        Log.d(LOG_TAG, "query = $uri")
        return when (uriMatcher!!.match(uri)) {
            SEARCH_SUGGEST -> {
                Log.d(LOG_TAG, "Search suggestions requested.")
                val cursor = MatrixCursor(SEARCH_SUGGEST_COLUMNS, 1)
                cursor.addRow(
                    arrayOf(
                        "1", "Search Result", "Search Result Description", "content_id"
                    )
                )
                cursor
            }
            else -> throw IllegalArgumentException("Unknown Uri: $uri")
        }
    }

}
