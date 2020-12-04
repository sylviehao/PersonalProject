package com.sylvie.boardgameguide.login

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.data.User

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    private val _user = MutableLiveData<User>()

    val user: LiveData<User>
        get() = _user

    var userToken: String? = null
        get() = GameApplication.appContext
            ?.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            ?.getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    GameApplication.appContext
                        ?.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)?.edit()
                        ?.remove(USER_TOKEN)
                        ?.apply()
                    null
                }
                else -> {
                    GameApplication.appContext
                        ?.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)?.edit()
                        ?.putString(USER_TOKEN, value)
                        ?.apply()
                    value
                }
            }
        }

    /**
     * It can be use to check login status directly
     */
    val isLoggedIn: Boolean
        get() = userToken != null

    /**
     * Clear the [userToken] and the [user]/[_user] data
     */
    fun clear() {
        userToken = null
        _user.value = null
    }


}