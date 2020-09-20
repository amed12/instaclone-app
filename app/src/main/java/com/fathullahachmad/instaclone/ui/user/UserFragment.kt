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

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.utils.AppWireframe
import com.fathullahachmad.instaclone.utils.KEY_LOG_OUT
import com.fathullahachmad.instaclone.utils.dialog.DialogListener
import com.fathullahachmad.instaclone.utils.dialog.DialogLoading
import com.fathullahachmad.instaclone.utils.dialog.DialogMenu
import com.fathullahachmad.instaclone.utils.showToast
import com.vascomm.vascwork.architecture.core.Result
import com.vascomm.vascwork.architecture.core.ViewStateInterface
import kotlinx.android.synthetic.main.toolbar_user.*

class UserFragment : Fragment(R.layout.fragment_user), DialogListener, ViewStateInterface {
    private val dialogPopUp by lazy { context?.let { DialogMenu(it, this) } }
    private val dialogLoading by lazy { context?.let { DialogLoading(it) } }
    private val userModule by lazy { context?.let { UserModule(it, this) } }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_menu?.setOnClickListener {
            dialogPopUp?.show(mutableListOf(KEY_LOG_OUT), true, isCancelable = true)
        }
    }

    override fun onDialogAction(tag: String, data: Any?) {
        if (tag == KEY_LOG_OUT) userModule?.logout()
    }

    override fun onFailure(result: Result) {
        context?.showToast(result.message)
    }

    override fun onLoading(result: Result) {
        dialogLoading?.show(result.isLoading)
    }

    override fun onSuccess(result: Result) {
        result.apply {
            when (tag) {
                UserModule.TAG_LOGOUT -> AppWireframe().common.root(context)
                else -> context?.showToast(message)
            }
        }
    }
}