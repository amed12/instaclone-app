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

package com.fathullahachmad.instaclone.utils

import java.text.SimpleDateFormat
import java.util.*

/* default key for service */
const val KEY_DATA = "data"
const val KEY_MESSAGE = "message"
const val KEY_STATUS = "status"
const val KEY_CODE = "code"
const val KEY_LOG_OUT = "log-out"

//Data network
const val KEY_USERNAME = "username"
const val KEY_FULL_NAME = "fullname"
const val KEY_WEBSITE = "website"
const val KEY_BIO = "bio"
const val KEY_PHONE = "phone"
const val KEY_GENDER = "gender"
const val KEY_IMAGE_PROFILE = "image-profile"
const val KEY_EMAIL = "email"
const val KEY_PASS = "pass"


val COMPLEX_DATE_FORMAT = SimpleDateFormat("EEEE, dd MMMM yyyy hh:mm", Locale("id"))
val FULL_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("id"))
val STANDARD_DATE_FORMAT = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id"))
val SIMPLE_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
val DAY_DATE_FORMAT = SimpleDateFormat("EEEE", Locale("id"))