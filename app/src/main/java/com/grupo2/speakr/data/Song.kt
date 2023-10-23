package com.grupo2.speakr.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Song (
    val id: Int,
    val title: String,
    val author: String,
    val url: String,
): Parcelable
