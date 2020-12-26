package com.sylvie.boardgameguide.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sylvie.boardgameguide.GameApplication
import com.sylvie.boardgameguide.data.User

object UserManager {
    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    val user = MutableLiveData<User>()
//    val user: LiveData<User>
//        get() = _user

    var userToken: String? = null
        get() = GameApplication.appContext
            ?.getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            ?.getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    GameApplication.appContext
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    GameApplication.appContext
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }
    val isLoggedIn: Boolean
        get() = userToken != null

    fun clear() {
        userToken = null
//        _user.value = null
    }
}