package workwork.company.wstest.domain

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.*
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val AUTH_TOKEN_KEY = "authToken"
    private val FAVORITE_IDS_KEY = "favoriteIds"

    fun saveUserName(name: String) {
        editor.putString("userName", name)
        editor.apply()
    }

    fun getUserName(): String? {
        return sharedPreferences.getString("userName", null)
    }
    fun saveAuthToken(token: String) {
        editor.putString(AUTH_TOKEN_KEY, token)
        editor.apply()
    }
    fun saveFavoriteIds(ids: List<String>) {
        val json = gson.toJson(ids)
        editor.putString(FAVORITE_IDS_KEY, json)
        editor.apply()
    }

    fun getFavoriteIds(): List<String> {
        val json = sharedPreferences.getString(FAVORITE_IDS_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    fun addFavoriteId(id: String) {
        val currentIds = getFavoriteIds().toMutableList()
        if (!currentIds.contains(id)) {
            currentIds.add(id)
            saveFavoriteIds(currentIds)
        }
    }
    fun getAuthToken(): String? {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null)
    }

    fun clearAuthToken() {
        editor.remove(AUTH_TOKEN_KEY)
        editor.apply()
    }
}
