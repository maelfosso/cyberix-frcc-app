package com.cyberix.beac.frc

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.cyberix.beac.frc.ui.theme.FRCTheme
import com.cyberix.beac.frc.utils.JWTManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var jwtManager: JWTManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val intent: Intent

            Log.d("MainActivity", "JWT Valid: ${jwtManager.isJwtValid()}")
            if (!jwtManager.isJwtValid()) {
                intent = Intent(this@MainActivity, StartActivity::class.java)
            } else {
                intent = Intent(this@MainActivity, HomeActivity::class.java)
            }

            startActivity(intent)
            finish()
        }

//        val intent = Intent(this, StartActivity::class.java)
//        startActivity(intent)
//        finish()
//        setContent {
//            FRCTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
    }

//    private suspend fun checkJwtAndNavigate() {
//        kotlinx.coroutines.delay(50)
//        val jwtString = cookieManager.getCookies()
//
//        if (jwtString == null || jwtString.isEmpty() || !isJwtValid(jwtString)) {
//            val intent = Intent(this@MainActivity, LoginActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
//
//    private fun isJwtValid(jwtString: String): Boolean {
//        return try {
//            val jwt = JWT(jwtString)
//            !jwt.isExpired(0)
//        } catch (e: Exception) {
//            false
//        }
//    }

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
    FRCTheme {
        Greeting("Android")
    }
}