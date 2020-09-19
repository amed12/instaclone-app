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

package com.fathullahachmad.instaclone.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.data.pref.AppSession
import com.fathullahachmad.instaclone.utils.AppWireframe
import com.fathullahachmad.instaclone.utils.setGradationColor
import kotlinx.android.synthetic.main.activity_main.*

class RootActivity : AppCompatActivity() {
    private val wireFrame by lazy { AppWireframe() }
    private val session by lazy { AppSession(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_splash.setGradationColor()
    }

    override fun onStart() {
        super.onStart()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            checkLogin()
        }, 2000)
    }

    private fun checkLogin() {
        val loggedIn = session.getBoolean(AppSession.KEY_LOGIN)
        when {
            !loggedIn -> {
                wireFrame.common.login(this)
                finish()
            }
            else -> {
                wireFrame.common.main(this)
                finish()
            }
        }
    }

}