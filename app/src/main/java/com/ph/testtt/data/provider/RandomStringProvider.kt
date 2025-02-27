package com.ph.testtt.data.provider

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.ph.testtt.data.room.RandomStringEntity
import org.json.JSONObject

class RandomStringProvider(private val context: Context) {

    private val uri: Uri = Uri.parse("content://com.iav.contestdataprovider/text")


    fun fetchRandomString(maxLength: Int): RandomStringEntity? {
        val contentResolver: ContentResolver = context.contentResolver
        val projection = arrayOf("data") // The column we need

        val bundle = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, maxLength)
        }
        val startTime = System.currentTimeMillis()
        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, bundle.toString())

        val endTime = System.currentTimeMillis()
        val timeTaken = endTime - startTime

        Log.d("ProviderPerformance", "Time taken to fetch data: ${timeTaken}ms")
        return cursor?.use {
            if (it.moveToFirst()) {
                val jsonString = it.getString(it.getColumnIndexOrThrow("data"))
                Log.d("ProviderResponse", "Received: $jsonString")
                parseJson(jsonString, maxLength)
            } else {
                Log.e("ProviderResponse", "No data received")
                null
            }
        }
    }


    private fun parseJson(jsonString: String, maxLength: Int): RandomStringEntity {
        val jsonObject = JSONObject(jsonString).getJSONObject("randomText")
        val fullValue = jsonObject.getString("value")

        // Ensure the string does not exceed maxLength
        val truncatedValue = if (fullValue.length > maxLength) {
            fullValue.substring(0, maxLength)
        } else {
            fullValue
        }
        return RandomStringEntity(
            value = truncatedValue,
            length = truncatedValue.length,
            created = jsonObject.getString("created"),
            isFavourite =  false
        )
    }
}


