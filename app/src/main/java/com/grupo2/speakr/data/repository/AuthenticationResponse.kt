package com.grupo2.speakr.data.repository

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AuthenticationResponse (
    val email: String,
    val accessToken: String,
): Parcelable