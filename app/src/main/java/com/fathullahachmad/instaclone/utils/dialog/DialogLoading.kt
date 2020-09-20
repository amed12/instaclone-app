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

package com.fathullahachmad.instaclone.utils.dialog

import android.app.Dialog
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.utils.lifecycleOwner

class DialogLoading(context: Context) : Dialog(context) {
    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(R.layout.dialog_loading)
    }

    private inner class ModuleLifecycleObserver : LifecycleObserver {
        fun addObserver(lifecycle: Lifecycle) = lifecycle.addObserver(this)

        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() = dismiss()

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() = dismiss()
    }

    fun addLifecyclerObserver(lifecycle: Lifecycle) {
        ModuleLifecycleObserver().addObserver(lifecycle)
    }

    fun show(isShown: Boolean, isCancelable: Boolean = false) {
        if (!super.isShowing()) {
            setCancelable(isCancelable)
            context.lifecycleOwner()?.lifecycle?.let { addLifecyclerObserver(it) }
            if (isShown) super.show()
            else super.dismiss()
        } else {
            super.dismiss()
        }
    }

    override fun show() {
        show(true)
    }
}