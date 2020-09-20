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

package com.fathullahachmad.instaclone.data.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Users(
    @SerializedName("bio")
    val bio: String = "", // null
    @SerializedName("email")
    val email: String = "", // amedixro@gmail.com
    @SerializedName("fullname")
    val fullname: String = "", // null
    @SerializedName("gender")
    val gender: String = "", // null
    @SerializedName("image-profile")
    val imageProfile: String = "", // null
    @SerializedName("phone")
    val phone: String = "", // null
    @SerializedName("username")
    val username: String = "", // achmad
    @SerializedName("website")
    val website: String = "" // null
)