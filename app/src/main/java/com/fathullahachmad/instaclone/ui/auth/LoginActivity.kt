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

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.ui.ui.login.LoggedInUserView
import com.fathullahachmad.instaclone.ui.ui.login.LoginViewModel
import com.fathullahachmad.instaclone.ui.ui.login.LoginViewModelFactory
import com.fathullahachmad.instaclone.utils.AppWireframe
import com.fathullahachmad.instaclone.utils.intent
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_go_to_register?.setOnClickListener {
            startActivity(intent(RegisterActivity::class.java))
        }
        val login = findViewById<Button>(R.id.login)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.apply {
                if (loginState.isDataValid) {
                    isEnabled = true
                    background.setTint(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.colorAccent
                        )
                    )
                    val wireframe = AppWireframe().common
                    setOnClickListener {
                        wireframe.main(this@LoginActivity)
                    }
                } else {
                    isEnabled = false
                    background.setTint(
                        ContextCompat.getColor(
                            this@LoginActivity,
                            R.color.colorAccentSecondary
                        )
                    )
                    setOnClickListener {
                        //
                    }
                }

            }

            if (loginState.usernameError != null) {
                til_username_login.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                til_password_login.error = getString(loginState.passwordError)
            }
        })

        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

//            loading.visibility = View.GONE
            if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            }
            setResult(Activity.RESULT_OK)

            //Complete and destroy login activity once successful
            finish()
        })

        edt_username_register.afterTextChanged {
            loginViewModel.loginDataChanged(
                edt_username_register.text.toString(),
                edt_password_register.text.toString()
            )
        }

        edt_password_register.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    edt_username_register.text.toString(),
                    edt_password_register.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            edt_username_register.text.toString(),
                            edt_password_register.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
//                loading.visibility = View.VISIBLE
                loginViewModel.login(edt_username_register.text.toString(), edt_password_register.text.toString())
            }
        }
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome)
        val displayName = model.displayName
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}