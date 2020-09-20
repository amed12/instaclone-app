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

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.utils.*
import com.fathullahachmad.instaclone.utils.dialog.DialogLoading
import com.vascomm.vascwork.additional.validation.RequireRule
import com.vascomm.vascwork.additional.validation.Validation
import com.vascomm.vascwork.additional.validation.ValidationInterface
import com.vascomm.vascwork.architecture.core.Result
import com.vascomm.vascwork.architecture.core.ViewStateInterface
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), ViewStateInterface, ValidationInterface {
    private val validation by lazy { Validation(this) }
    private val authModule by lazy { AuthModule(this, this) }
    private val dialogLoading by lazy { DialogLoading(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        tv_go_to_register?.setOnClickListener {
            startActivity(intent(RegisterActivity::class.java))
        }
        checkStateButton()
        validation.apply {
            registerField(
                edt_username_register, til_username_login, KEY_EMAIL, mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field))
                )
            )
            registerField(
                edt_password_register, til_password_login, KEY_PASS, mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field))
                )
            )
        }
        edt_password_register.apply {
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> checkStateButton()
                }
                false
            }
        }
    }

    private fun checkStateButton() {
        login.apply {
            if (anyNotNull(
                    edt_username_register.text.toString(),
                    edt_password_register.text.toString()
                )
            ) {
                isEnabled = true
                background.setTint(
                    ContextCompat.getColor(
                        this@LoginActivity,
                        R.color.colorAccent
                    )
                )
                setOnClickListener {
                    validation.validation()
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

    }

    override fun onFailure(result: Result) {
        result.apply {
            when {
                message.contains("no user record") -> {
                    til_username_login.error = message
                }
                message.contains("The password is invalid") -> {
                    til_password_login.error = message
                }
                else -> {
                    showToast(message)
                }
            }
        }
    }

    override fun onLoading(result: Result) {
        dialogLoading.show(result.isLoading)
    }

    override fun onSuccess(result: Result) {
        AppWireframe().common.main(this)
    }

    override fun validationSuccess(data: HashMap<String, String>) {
        authModule.login(data[KEY_EMAIL].toString(), data[KEY_PASS].toString())
    }


}
