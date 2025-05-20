package com.gmlwo22.spotifyclone.utils

import android.util.Base64
import java.security.MessageDigest


fun generateCodeVerifier(): String {
   val allowed = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
    return (1..64).map { allowed.random() }.joinToString("")
}
fun generateCodeChallenge(verifier: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(verifier.toByteArray())
    return Base64.encodeToString(bytes, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
}