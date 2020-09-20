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

package com.fathullahachmad.instaclone.ui.user

import android.content.Context
import com.fathullahachmad.instaclone.data.model.Users
import com.fathullahachmad.instaclone.data.pref.AppSession
import com.fathullahachmad.instaclone.utils.connectionProblemMessage
import com.fathullahachmad.instaclone.utils.errorMessage
import com.google.firebase.auth.FirebaseAuth
import com.vascomm.vascwork.architecture.core.Module
import com.vascomm.vascwork.architecture.core.ViewStateInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

interface IUserModule {
    fun logout()
    fun getUsers(): Users
}

class UserModule(context: Context, viewState: ViewStateInterface) : Module(viewState, context),
    IUserModule {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val session by lazy { AppSession(context) }

    companion object {
        const val TAG_LOGOUT = "tag-logout"
    }

    override fun logout() {
        scope.launch(Dispatchers.Default) {
            event(TAG_LOGOUT) {
                try {
                    it.loading(isLoading = true)
                    firebaseAuth.signOut()
                    session.destroySession()
                    it.loading(isLoading = false)
                    it.success("success")
                } catch (e: java.lang.Exception) {
                    when (e) {
                        is SocketTimeoutException -> {
                            it.loading(isLoading = false)
                            it.failure("", message = context.errorMessage()) // "Connection Timeout"
                        }
                        is IOException -> {
                            it.loading(isLoading = false)
                            it.failure("", message = context.connectionProblemMessage())
                        }
                        else -> {
                            it.loading(isLoading = false)
                            it.failure("", message = e.message.toString())
                        }
                    }
                }
            }
        }
    }

    override fun getUsers(): Users {
        return session.getUsers()
    }
}