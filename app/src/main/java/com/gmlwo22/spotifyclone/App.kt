package com.gmlwo22.spotifyclone

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gmlwo22.spotifyclone.ui.screens.home.HomeScreen
import com.gmlwo22.spotifyclone.ui.screens.login.LoginScreen
import com.gmlwo22.spotifyclone.utils.generateCodeChallenge
import com.gmlwo22.spotifyclone.utils.generateCodeVerifier

@Composable
fun App(authViewModel: AuthViewModel) {
    val navController = rememberNavController()
    val authState = authViewModel.authState
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            LaunchedEffect(authState) {
                when (authState) {
                    AuthState.Authenticated -> navController.navigate("home") {
                        popUpTo("splash") { inclusive = true }
                    }
                    AuthState.Unauthenticated -> navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                    else -> {}
                }
            }
        }
        composable("login") {
            val context = LocalContext.current
            LoginScreen(onLoginClicked = { launchSpotifyLogin(context) })
        }
        composable("home") {
            HomeScreen()
        }
    }
}

fun launchSpotifyLogin(context: Context) {
    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val redirectUri = "heejaesportifyclone://callback"
    val codeVerifier = generateCodeVerifier()
    val codeChallenge = generateCodeChallenge(codeVerifier)
    val authUrl = Uri.Builder()
        .scheme("https")
        .authority("accounts.spotify.com")
        .appendPath("authorize")
        .appendQueryParameter("client_id", clientId)
        .appendQueryParameter("response_type", "code")
        .appendQueryParameter("redirect_uri", redirectUri)
        .appendQueryParameter("code_challenge_method", "S256")
        .appendQueryParameter("code_challenge", codeChallenge)
        .appendQueryParameter("scope", "user-read-email user-read-private streaming")
        .build()
        .toString()

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(authUrl))
    context.startActivity(intent)
}