package com.cyberix.beac.frc

import android.content.Intent
import android.os.Bundle
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
import androidx.lifecycle.lifecycleScope
import com.cyberix.beac.frc.ui.theme.FRCTheme
import com.cyberix.beac.frc.utils.CookieManager
import com.cyberix.beac.frc.utils.JWTManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    @Inject
    lateinit var jwtManager: JWTManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            var intent: Intent

            if (!jwtManager.isJwtValid()) {
                intent = Intent(this@HomeActivity, StartActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                setContent {
                    FRCTheme {
                        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                            Greeting3(
                                name = "Android",
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    FRCTheme {
        Greeting3("Android")
    }
}