package com.gmlwo22.spotifyclone

import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.gmlwo22.spotifyclone.ui.theme.SpotifyCloneTheme
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.protocol.client.Subscription
import com.spotify.protocol.types.PlayerState
import com.spotify.protocol.types.Track
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationRequest
import com.spotify.sdk.android.auth.AuthorizationResponse

class MainActivity : ComponentActivity() {
    val clientId = BuildConfig.SPOTIFY_CLIENT_ID
    val clientSecret = BuildConfig.SPOTIFY_CLIENT_SECRET
    val redirectUrl = "heejaesportifyclone://callback"
    private var spotifyAppRemote: SpotifyAppRemote? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpotifyCloneTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d("MainActivity", "OnStart")
        val builder = AuthorizationRequest.Builder(clientId, AuthorizationResponse.Type.CODE, redirectUrl)
        builder.setScopes(arrayOf("streaming"))
        val request = builder.build()
        AuthorizationClient.openLoginActivity(this, 1337, request)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?, caller: ComponentCaller) {
        super.onActivityResult(requestCode, resultCode, data, caller)
        if (requestCode == 1337) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            Log.d("MainActivity", "#### ${response.type}")
            when (response.type) {
                AuthorizationResponse.Type.CODE -> {
                    val token = response.accessToken
                    Log.d("MainActivity", "#### $token")
                }
                AuthorizationResponse.Type.ERROR -> {

                }
                else -> {

                }
            }
        }
    }
    override fun onStop() {
        super.onStop()
        Log.d("MainActivity", "onStop")

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpotifyCloneTheme {
        Greeting("Android")
    }
}