package com.krayapp.dndworkaround

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract


class StarredChecker(private val context: Context) {
    fun getStarredContactNumbers(): List<String> {
        val starredContactNumbers = mutableListOf<String>()

        val uri: Uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.STARRED
        )

        val selection = "${ContactsContract.Contacts.STARRED} = ?"
        val selectionArgs = arrayOf("1")

        val cursor: Cursor? = context.contentResolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

            while (cursor.moveToNext()) {
                val number = cursor.getString(numberIndex)
                starredContactNumbers.add(number)
            }
        }

        return starredContactNumbers
    }
}