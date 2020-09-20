/*
 * Copyright (C) 2020 Achmad Fathullah - PDA
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.fathullahachmad.instaclone.data.pref

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

class AppSession(context: Context) {
    private var session = context.getSharedPreferences(KEY_SESSION, 0)
    private var edit = session.edit()

    companion object {
        const val KEY_SESSION = "app-session"
        const val KEY_TOKEN = "key-token"
        const val KEY_USER = "user-data"
        const val KEY_USER_EDIT = "user-edit"
        const val KEY_REMEMBER = "remember-me"
        const val KEY_USERNAME = "user-name"
        const val KEY_LOGIN = "has-login"
        const val KEY_COMM_FEATURE = "community-feature"
        const val KEY_INTRO = "intro"
    }

    /* store user data and token*/
    fun createSession(dataUser: JSONObject) {
        edit.apply {
            putString(KEY_USER, dataUser.toString())
            commit()
        }
    }

    fun destroySession() {
        edit.remove(KEY_USER)
        putBoolean(KEY_LOGIN, false)
    }

    fun putString(key: String, data: String) {
        edit?.putString(key, data)?.apply()
    }

    fun putBoolean(key: String, status: Boolean) {
        edit.putBoolean(key, status)
        edit.apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false) =
        session?.getBoolean(key, defaultValue) ?: defaultValue

    fun getJsonArray(key: String, defaultValue: String = "") =
        JSONArray(session?.getString(key, defaultValue) ?: defaultValue)

}
