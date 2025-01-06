package com.cyberix.beac.frc.ui.start.otp

//noinspection UsingMaterialAndMaterial3Libraries
import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cyberix.beac.frc.HomeActivity
import com.cyberix.beac.frc.R
import com.cyberix.beac.frc.StartActivity
import com.cyberix.beac.frc.data.network.ApiState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationScreen(
    navController: NavController,
    email: String?,
    modifier: Modifier = Modifier,
    otpViewModel: OtpViewModel = hiltViewModel()
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }

    val otpUIState by otpViewModel.uiState.collectAsState()
    val requestState by otpViewModel.requestState.collectAsState()

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    androidx.compose.material3.Button(
                        onClick = {
                            navController.navigateUp()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                title = {}
            )
        }
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
                androidx.compose.material3.Text(
                    modifier = Modifier.padding(16.dp),
                    text = "One-Time-Password Verification",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Vous avez reçu un code à 6 chiffres par email. Veuillez le saisir, s'il vous plait",
                    // fontSize = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    otpUIState.digits.forEachIndexed { index, value ->
                        OutlinedTextField(
                            value = value,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) { // Autorise seulement les chiffres
                                    val mutableOtpValues = otpUIState.digits.toMutableList()
                                    mutableOtpValues[index] = newValue
                                    // otpValues = mutableOtpValues.toList()

                                    otpViewModel.updateForm(
                                        otpUIState.copy(
                                            digits = mutableOtpValues,
                                            otp = mutableOtpValues.joinToString(""),
                                            email = "$email"
                                        )
                                    )

                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                }
                            },
                            modifier = Modifier
                                .width(48.dp)
                                .focusRequester(focusRequesters[index]),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = androidx.compose.ui.text.TextStyle(textAlign = TextAlign.Center),
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        //                coroutineScope.launch {
                        //                    if (otpValues.all { it.isNotEmpty() }) {
                        //                        val otp = otpValues.joinToString("")
                        //                        verificationMessage = verifyOTP(otp)
                        //                    } else {
                        //                        verificationMessage = "Veuillez entrer les 6 chiffres."
                        //                    }
                        //                }

                        otpViewModel.submit()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Vérifier l'OTP")
                }

                when (requestState) {
                    is ApiState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            androidx.compose.material3.CircularProgressIndicator()
                        }
                    }

                    is ApiState.Success -> {
                        context.startActivity(Intent(context, HomeActivity::class.java))
                        (context as Activity).finish()
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
    }
}


suspend fun verifyOTP(otp: String): String? {
    delay(1000)
    return if (otp == "123456") {
        null
    } else {
        "OTP invalide. Veuillez réessayer."
    }
}
