package io.felipe.appetit.database

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import io.felipe.appetit.R

private const val DATABASE_NAME = "database"

class PrefsDb {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences
        private lateinit var mainJson: String

        fun init(context: Context): Companion {
            if (!Companion::sharedPreferences.isInitialized) {
                sharedPreferences =
                    context.getSharedPreferences("appetit_prefs", Context.MODE_PRIVATE)
                mainJson = getMainJson(context)
            }
            return this
        }

        fun getDatabase(): Database {
            val json = sharedPreferences.getString(DATABASE_NAME, "")
            if (json?.isEmpty() == true) return initDatabase()
            return Gson().fromJson(json, Database::class.java)
        }

        fun saveDatabase(obj: Database) {
            sharedPreferences.edit().putString(DATABASE_NAME, Gson().toJson(obj)).apply()
        }

        private fun initDatabase(): Database {
            val json = sharedPreferences.getString(DATABASE_NAME, "")
            return if (json?.isEmpty() == true) {
                sharedPreferences.edit().putString(DATABASE_NAME, mainJson).apply()
                Gson().fromJson(mainJson, Database::class.java)
            } else {
                Gson().fromJson(json, Database::class.java)
            }
        }

        private fun getMainJson(context: Context): String =
            context.resources.openRawResource(R.raw.seed).bufferedReader().use { it.readText() }

    }
}