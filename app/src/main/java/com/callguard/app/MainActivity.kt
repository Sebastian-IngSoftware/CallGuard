package com.callguard.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.callguard.app.ui.navigation.CallGuardNavHost
import com.callguard.app.ui.theme.CallGuardTheme
import com.callguard.app.ui.theme.NeuBackground
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // La app es siempre oscura: barras transparentes con iconos claros.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )

        setContent {
            CallGuardTheme {
                // El Surface pinta toda la ventana (incluido lo que va detrás de
                // las barras); el Box interior aplica el safe area para que
                // ninguna pantalla se meta bajo la barra de estado o la de
                // navegación.
                Surface(modifier = Modifier.fillMaxSize(), color = NeuBackground) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                    ) {
                        CallGuardNavHost()
                    }
                }
            }
        }
    }
}