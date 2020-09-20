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
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.fathullahachmad.instaclone.R
import com.fathullahachmad.instaclone.utils.lifecycleOwner
import com.fathullahachmad.instaclone.utils.viewholder.ListDialogViewHolder
import com.vascomm.vascwork.architecture.adapter.RecyclerViewAdapter
import kotlinx.android.synthetic.main.dialog_list_category.*

class DialogMenu(context: Context, dialogListener: DialogListener? = null) : Dialog(context),
    LifecycleObserver {
    private val strings = ArrayList<String>()
    private val adapter by lazy {
        object : RecyclerViewAdapter<String, ListDialogViewHolder>(
            R.layout.item_list,
            ListDialogViewHolder::class.java,
            strings
        ) {
            override fun bindView(holder: ListDialogViewHolder, model: String, position: Int) {
                holder.bind(model)
                holder.itemView.setOnClickListener {
                    dialogListener?.onDialogAction(model, position)
                    dismiss()
                }
            }
        }
    }

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
    }

    fun show(list: MutableList<String>, isShown: Boolean, isCancelable: Boolean = false) {
        if (window != null) {
            setContentView(R.layout.dialog_list_category)
            rv_list_dialog?.adapter = adapter
            strings.clear()
            strings.addAll(list)
            adapter.notifyDataSetChanged()
            val wlp = window?.attributes
            wlp?.gravity = Gravity.CENTER
            window?.attributes = wlp
            window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window?.setBackgroundDrawable(
                    ColorDrawable(
                        context.resources.getColor(
                            R.color.black80alpha,
                            context.theme
                        )
                    )
                )
            } else {
                window?.decorView?.setBackgroundColor(context.resources.getColor(R.color.black80alpha))
            }
            if (!isShowing) {
                if (isShown) {
                    setCancelable(isCancelable)
                    setCanceledOnTouchOutside(isCancelable)
                    context.lifecycleOwner()?.lifecycle?.let { addLifecyclerObserver(it) }
                    show()
                } else dismiss()
            } else
                dismiss()

        } else
            dismiss()

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

}