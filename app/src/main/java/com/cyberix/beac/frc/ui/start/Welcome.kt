package com.cyberix.beac.frc.ui.start

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cyberix.beac.frc.R

@Preview(showBackground = true)
@Composable
fun WelcomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val logoCEMAC = painterResource(R.drawable.logo_cemac)

    Surface(modifier = modifier) {
        Column(
            modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column (
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = logoCEMAC,
                    contentDescription = null
                )
                // Text("Bienvenue au ")
                Text("Forum Regional de Cybersecurité de la CEMAC",
                    style = TextStyle(
                        fontSize = 40.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
            Column (
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Avant toute chose, si vous ne l'avez pas encore fait, s'il vous plait, ",
                    textAlign = TextAlign.Center
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navController.navigate(StartNavigationItem.Register.route)
                    }
                ) {
                    Text("Enregistez-vous")
                }
            }
            Row (
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Déjà enregistré? ")
                Text(
                    text = "Connectez-vous!",
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        color = Color(android.graphics.Color.parseColor("#a1853e"))
                    ),
                    modifier = Modifier.clickable(
                        onClick = {
                            navController.navigate(StartNavigationItem.Login.route)
                            // val email = "test.user@example.com"
                            // val encodedEmail = Uri.encode(email)
                            // navController.navigate("${StartNavigationItem.Otp.route}/$encodedEmail")
                        }
                    )
                )
            }
        }
    }
}