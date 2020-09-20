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

package com.fathullahachmad.instaclone.ui.auth

import android.content.Context
import com.fathullahachmad.instaclone.data.model.Users
import com.fathullahachmad.instaclone.data.pref.AppSession
import com.fathullahachmad.instaclone.data.pref.AppSession.Companion.KEY_LOGIN
import com.fathullahachmad.instaclone.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.*
import com.vascomm.vascwork.architecture.core.Module
import com.vascomm.vascwork.architecture.core.ViewStateInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException

interface IAuthModule {
    fun login(email: String, password: String)
    fun register(username: String, email: String, password: String)
}

class AuthModule(context: Context, viewState: ViewStateInterface) : Module(viewState, context),
    IAuthModule {
    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mDatabase by lazy { FirebaseDatabase.getInstance().reference.child("Users") }
    private val session by lazy { AppSession(context) }


    companion object {
        const val TAG_LOGIN = "tag-login"
        const val TAG_REGISTER = "tag-register"
    }

    override fun login(email: String, password: String) {
        scope.launch(Dispatchers.Default) {
            event(TAG_LOGIN) {
                try {
                    it.loading(isLoading = true)
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            when (task.isSuccessful) {
                                true -> {
                                    if (task.exception != null) {
                                        it.loading(isLoading = false)
                                        it.failure(message = task.exception?.message.toString())
                                    } else {
                                        val userId: String = firebaseAuth.currentUser?.uid ?: "0"
                                        mDatabase.addValueEventListener(object :
                                            ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                if (snapshot.hasChild(userId)) {
                                                    snapshot.getValue(Users::class.java)
                                                        ?.objectToJsonObjectString()
                                                        ?.stringJsonToJsonObject()?.let { it1 ->
                                                            session.createSession(
                                                                it1
                                                            )
                                                        }
                                                    session.putBoolean(KEY_LOGIN, true)
                                                    it.loading(isLoading = false)
                                                    it.success(message = "success")
                                                } else {
                                                    it.loading(isLoading = false)
                                                    it.failure(message = task.exception?.message.toString())
                                                }

                                            }

                                            override fun onCancelled(error: DatabaseError) {}

                                        })

                                    }
                                }
                                false -> {
                                    it.loading(isLoading = false)
                                    it.failure(message = task.exception?.message.toString())
                                }
                            }
                        }
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
                        is FirebaseAuthUserCollisionException -> {
                            it.loading(isLoading = false)
                            it.failure("", message = context.userAlreadyExistMessage())
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

    override fun register(username: String, email: String, password: String) {
        scope.launch(Dispatchers.Default) {
            event(TAG_REGISTER) {
                try {
                    it.loading(isLoading = true)
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            when (task.isSuccessful) {
                                true -> {

                                    if (task.exception != null) {
                                        it.loading(isLoading = false)
                                        it.failure(message = task.exception?.message.toString())
                                    } else {
                                        val userId: String = firebaseAuth.currentUser?.uid ?: "0"
                                        val currentUserDb: DatabaseReference =
                                            mDatabase.child(userId)
                                        currentUserDb.child(KEY_USERNAME).setValue(username)
                                        currentUserDb.child(KEY_IMAGE_PROFILE).setValue("null")
                                        currentUserDb.child(KEY_FULL_NAME).setValue("null")
                                        currentUserDb.child(KEY_WEBSITE).setValue("null")
                                        currentUserDb.child(KEY_BIO).setValue("null")
                                        currentUserDb.child(KEY_PHONE).setValue("null")
                                        currentUserDb.child(KEY_GENDER).setValue("null")
                                        currentUserDb.child(KEY_EMAIL).setValue(email)
                                        it.loading(isLoading = false)
                                        it.success(message = "success")
                                    }
                                }
                                false -> {
                                    it.loading(isLoading = false)
                                    it.failure(message = task.exception?.message.toString())
                                }
                            }
                        }
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
                        is FirebaseAuthUserCollisionException -> {
                            it.loading(isLoading = false)
                            it.failure("", message = context.userAlreadyExistMessage())
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
}