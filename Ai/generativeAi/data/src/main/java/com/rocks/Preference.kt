import android.content.Context
import android.content.SharedPreferences

object Preference {

    private const val APP_PREFERENCES_FILE_NAME = "ai.app_preferences"

    private fun getPreferencesInstance(context: Context): SharedPreferences {

        return context.getSharedPreferences(APP_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)

    }

    fun getString(context: Context,key: String): String {

        val sharedPreferences: SharedPreferences = getPreferencesInstance(context)

        return sharedPreferences.getString(key, "")?:""

    }

    fun getBoolean(context: Context,key: String): Boolean {

        val sharedPreferences: SharedPreferences = getPreferencesInstance(context)

        return sharedPreferences.getBoolean(key, false)

    }

    fun putString(context: Context,key: String?, value: String): Boolean {
        val sharedPreferences: SharedPreferences = getPreferencesInstance(context)
        return sharedPreferences.edit().putString(key, value).commit()
    }

    fun putBoolean(context: Context,key: String?, value: Boolean): Boolean {
        val sharedPreferences: SharedPreferences = getPreferencesInstance(context)
        return sharedPreferences.edit().putBoolean(key, value).commit()
    }
}