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
import com.vascomm.vascwork.additional.validation.*
import com.vascomm.vascwork.architecture.core.Result
import com.vascomm.vascwork.architecture.core.ViewStateInterface
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), ViewStateInterface, ValidationInterface {
    private val validation by lazy { Validation(this) }
    private val authModule by lazy { AuthModule(this, this) }
    private val dialogLoading by lazy { DialogLoading(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        checkStateButton()
        tv_back_to_login?.setOnClickListener {
            backToLogin()
        }
        val passwordPattern = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})"
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        edt_re_password_register.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE ->
                    checkStateButton()
            }
            false
        }
        validation.apply {
            registerField(
                edt_username_register, til_username_register, KEY_USERNAME, mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field))
                )
            )
            registerField(
                edt_email_register, til_email_register, KEY_EMAIL, mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field)),
                    RegexRule(getString(R.string.alert_email_rule), emailPattern)
                )
            )
            registerField(
                edt_password_register, til_password_register, "first-pass", mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field)),
                    RegexRule(getString(R.string.lbl_alert_password_rule), passwordPattern)
                )
            )
            registerField(
                edt_re_password_register, til_re_password_register, KEY_PASS, mutableListOf(
                    RequireRule(getString(R.string.lbl_warning_fill_field)),
                    ConfrimationRule(getString(R.string.text_pass_not_match), edt_password_register)
                )
            )
        }
    }

    private fun checkStateButton() {
        btn_register.apply {
            if (anyNotNull(
                    edt_username_register?.text.toString(),
                    edt_email_register?.text.toString(),
                    edt_password_register?.text.toString(),
                    edt_re_password_register?.text.toString()
                )
            ) {
                isEnabled = true
                background.setTint(
                    ContextCompat.getColor(
                        this@RegisterActivity,
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
                        this@RegisterActivity,
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
        showToast(result.message)
    }

    override fun onLoading(result: Result) {
        dialogLoading.show(result.isLoading)
    }

    override fun onSuccess(result: Result) {
        backToLogin()
    }

    override fun validationSuccess(data: HashMap<String, String>) {
        authModule.register(
            username = data[KEY_USERNAME].toString(),
            email = data[KEY_EMAIL].toString(),
            password = data[KEY_PASS].toString()
        )
    }

    private fun backToLogin() = AppWireframe().common.login(this)
}