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

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.net.Uri
import android.text.Spanned
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.text.HtmlCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.bumptech.glide.Glide.with
import com.fathullahachmad.instaclone.R
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun dateFormat(data: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale("id"))
    val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val date = format.parse(data)
    return sdf.format(date)
}

fun timeFormat(data: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("id"))
    val sdf = SimpleDateFormat("HH:mm", Locale("id"))
    val date = format.parse(data)
    return sdf.format(date)
}

object Log {
    fun d(TAG: String?, message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d(TAG, message.substring(start, end))
        }
    }
}

fun Context.intent(toClass: Class<*>) = Intent(this, toClass)
fun Context.lifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext
    } else {
        null
    }
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}


fun AppCompatActivity.setToolbar(toolbar: Toolbar, title: String) {
    this.setSupportActionBar(toolbar)
    supportActionBar?.title = title
    supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
}

fun readFileFroAssets(context: Context, fileName: String): String {
    var content = ""
    try {
        val stream = context.assets.open(fileName)
        val size = stream.available()
        val buffer = ByteArray(size)

        stream.read(buffer)
        stream.close()

        content = String(buffer)

    } catch (e: IOException) {
        e.printStackTrace()
    }
    return content
}

fun md5(s: String) = try {
    val md = MessageDigest.getInstance("MD5")
    val messageDigest = md.digest(s.toByteArray())
    val number = BigInteger(1, messageDigest)
    var md5 = number.toString(16)

    while (md5.length < 32)
        md5 = "0$md5"

    md5
} catch (e: NoSuchAlgorithmException) {
    e.printStackTrace()
    ""
}

fun formatToMd5(type: String): String {
    val id = "73488de97b259bb9ea7fbc8e"
    val secret = "3e9ed9316688c73329de729f6069aaf8"

    val cal = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy_HH_MM_dd", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    val format = dateFormat.format(cal.time)

    return when (type) {
        "client_id" -> md5("$id$format")
        "client_secret" -> md5(
            "$format$secret"
        )
        else -> ""
    }
}


fun <T> JSONObject.toObject(classOfT: Class<T>, isInsideData: Boolean = true): T =
    if (isInsideData)
        Gson().fromJson(this[KEY_DATA].toString(), classOfT)
    else
        Gson().fromJson(this.toString(), classOfT)

fun <T> JSONArray.toObjectList(classOfT: Class<T>, useExposed: Boolean = false): List<T> {
//    val gson = if (useExposed) GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
//    else GsonBuilder().setLongSerializationPolicy(LongSerializationPolicy.STRING).create()
    val gson = Gson()
    val list = java.util.ArrayList<T>()
    try {
        for (i in 0 until length()) {
            list.add(gson.fromJson(getJSONObject(i).toString(), classOfT))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return list
}


fun <T> toList(classOfT: Class<T>, data: JSONArray): List<T> {
    val list = ArrayList<T>()
    val gson = Gson()
    (0 until data.length()).mapTo(list) {
        gson.fromJson(data[it].toString(), classOfT)
    }
    return list
}

fun replaceUri(uri: Uri, bitmap: Bitmap, context: Context, quality: Int): Uri {
    val outputStream = context.contentResolver.openOutputStream(uri)
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
    return uri
}

fun getValueArray(data: Array<String>?): ArrayList<String> {
    val temp = java.util.ArrayList<String>()
    for (i in 3..data!!.size) {
        temp.add(data[i - 1])
    }
    return temp
}

fun Context?.noDataMessage() = this?.getString(R.string.lbl_no_data) ?: ""
fun Context?.errorMessage() = this?.getString(R.string.text_message_error_connection) ?: ""
fun Context?.connectionProblemMessage() =
    this?.getString(R.string.text_alert_internet_connection) ?: ""

fun JSONObject.message(): String = try {
    this.getString(KEY_MESSAGE)
} catch (e: Exception) {
    ""
}

fun JSONObject.dataObject(): JSONObject = try {
    this.getJSONObject(KEY_DATA)
} catch (e: Exception) {
    JSONObject()
}

fun JSONObject.dataArray(): JSONArray = try {
    this.getJSONArray(KEY_DATA)
} catch (e: Exception) {
    JSONArray()
}

fun JSONObject.code(): Int = this.getInt(KEY_STATUS)

fun NavDirections.navActionDirectly(view: View) {
    Navigation.findNavController(view).navigate(this)
}

fun Any.objectToJsonObjectString(): String = Gson().toJson(this)


fun String.objectToJsonObject(): JSONObject {
    return JSONObject(this)
}


fun String?.toHtml(): Spanned? {
    if (this.isNullOrEmpty()) return null
    return HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
}


fun ImageView.loadImageFromServer(url: String, context: Context) {
    with(context)
        .load(url)
        .centerCrop()
        .error(android.R.drawable.ic_menu_gallery)
        .dontAnimate()
        .into(this)
}

fun ImageView.loadCircleImageFromServer(url: String, context: Context?) {
    if (context != null) {
        with(context)
            .load(url)
            .centerCrop()
            .circleCrop()
            .error(R.drawable.ic_baseline_account_circle_24)
            .dontAnimate()
            .into(this)
    }
}


fun View.isVisible(boolean: Boolean) {
    this.visibility = if (boolean) View.VISIBLE else View.GONE
}

// Date Extensions
fun getDate(sdf: SimpleDateFormat, addDay: Int? = null): HashMap<String, String> {
    val map = HashMap<String, String>()
    val c: Calendar = Calendar.getInstance()
    map["first"] = sdf.format(c.time)

    if (addDay != null) {
        c.add(Calendar.DAY_OF_MONTH, addDay)
        map["last"] = sdf.format(c.time)
    } else map["last"] = sdf.format(c.time)
    return map
}

fun String.toDate(
    dateFormat: SimpleDateFormat = COMPLEX_DATE_FORMAT,
    timeZone: TimeZone? = null
): Date? {
    if (timeZone != null) dateFormat.timeZone = timeZone
    return dateFormat.parse(this)
}

fun Date.formatTo(dateFormat: SimpleDateFormat = COMPLEX_DATE_FORMAT): String {
    return dateFormat.format(this)
}

fun TextView.setGradationColor() {
    val textShader: Shader = LinearGradient(
        0f, 0f, width.toFloat(), this.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#FDB54E"),
            Color.parseColor("#64B678"),
            Color.parseColor("#478AEA"),
            Color.parseColor("#8446CC")
        ), null, Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}


