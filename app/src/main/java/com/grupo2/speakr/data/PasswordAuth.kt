package com.grupo2.speakr.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PasswordAuth (
    val oldPassword: String,
    val newPassword: String,
): Parcelable