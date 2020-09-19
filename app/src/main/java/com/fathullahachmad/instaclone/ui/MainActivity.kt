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
import androidx.appcompat.app.AppCompatActivity
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.utils.setGradationColor
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tv_splash.setGradationColor()
    }
    /*
    private val wireFrame by lazy { AppWireframe() }
    private val session by lazy { AppSession(this) }
    private val rootBear: RootBeer by lazy { RootBeer(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        tv_version_name?.text = getString(R.string.lbl_value_version_name, BuildConfig.VERSION_NAME)
    }

    override fun onStart() {
        super.onStart()
        val handler = Handler()
        handler.postDelayed({
            checkLogin()
        }, 2000)
    }

    private fun checkLogin() {
        val hasIntro = session.getBoolean(AppSession.KEY_INTRO)
        val loggedIn = session.getBoolean(AppSession.KEY_LOGIN)
        val isRootDevice = rootBear.isRooted && rootBear.isRootedWithBusyBoxCheck

        Log.d("root-check", "$hasIntro, $loggedIn, $isRootDevice")
        when {
            !isRootDevice && !loggedIn && !hasIntro -> {
                session.putBoolean(AppSession.KEY_INTRO, false)
                wireFrame.common.intro(this)
                finish()
            }
            //TODO: Make dialog rooted device
            isRootDevice && !loggedIn && !hasIntro -> {
                Toast.makeText(
                    this,
                    getString(R.string.text_alert_root_detected),
                    Toast.LENGTH_SHORT
                ).show()
            }
            !isRootDevice && !loggedIn && hasIntro -> {
                wireFrame.common.login(this)
                finish()
            }
            else -> {
                wireFrame.common.main(this)
                finish()
            }
        }
    }
     */
}