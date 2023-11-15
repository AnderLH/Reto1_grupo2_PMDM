package com.grupo2.speakr.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class MailAuth (
    val oldMail: String,
    val newMail: String,
): Parcelable