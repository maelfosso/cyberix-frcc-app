package com.cyberix.beac.frc.ui.start.confirm

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cyberix.beac.frc.R
import com.cyberix.beac.frc.data.network.ApiState
import com.cyberix.beac.frc.ui.start.StartNavigationItem
import com.cyberix.beac.frc.ui.start.login.LoginViewModel

@Composable
fun ConfirmRegisterScreen(
    navController: NavController,
    token: String,
    confirmRegisterViewModel: ConfirmRegisterViewModel = hiltViewModel()
) {
    val requestState by confirmRegisterViewModel.requestState.collectAsState()

    val context = LocalContext.current

    Scaffold(
        topBar = {}
    ) { innerPadding ->
        val logoCEMAC = painterResource(R.drawable.logo_cemac)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = logoCEMAC,
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Confirmation",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }

            Text(
                "Appuyez pour confirmer votre enregistrement"
            )
            Button(
                onClick = { confirmRegisterViewModel.confirm(token) }
            ) {
                Text("Confirmez")
            }
        }


        when (requestState) {
            is ApiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is ApiState.Success -> {
                navController.navigate(StartNavigationItem.Welcome) {
                    popUpTo(StartNavigationItem.Welcome) { inclusive = true }
                }
            }

            is ApiState.Error -> {
                val error = (requestState as ApiState.Error).errorResponse
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

}