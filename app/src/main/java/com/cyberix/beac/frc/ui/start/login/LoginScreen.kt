package com.cyberix.beac.frc.ui.start.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cyberix.beac.frc.HomeActivity
import com.cyberix.beac.frc.R
import com.cyberix.beac.frc.data.network.ApiState
import com.cyberix.beac.frc.ui.start.StartNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen (
    navController: NavController,
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginUIState by loginViewModel.uiState.collectAsState()
    val requestState by loginViewModel.requestState.collectAsState()

    val context = LocalContext.current

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Button(
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
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "Connexion",
                    style = MaterialTheme.typography.displaySmall,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            LoginForm(
                loginUIState,
                onChanged = { newState ->
                    loginViewModel.updateForm(newState)
                },
                onSubmit = { loginViewModel.submit() }
            )
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
                val encodedEmail = Uri.encode(loginUIState.email)
                navController.navigate("${StartNavigationItem.Otp.route}/$encodedEmail") {
                    popUpTo(StartNavigationItem.Login.route) { inclusive = true }
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

@Composable
fun LoginForm(
    loginUIState: LoginUIState,
    onChanged: (LoginUIState) -> Unit,
    onSubmit: () -> Unit,
) {
    Box {
        val context = LocalContext.current

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LoginField(
                value = loginUIState.email,
                onChange = { value -> onChanged(
                    loginUIState.copy(email = value)
                )},
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = {
                    onSubmit()
                },
                enabled = loginUIState.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connectez-vous")
            }
        }
    }
}

fun checkCredentials(creds: LoginUIState, context: Context): Boolean {
    if (creds.isNotEmpty()) {
        context.startActivity(Intent(context, HomeActivity::class.java))
        (context as Activity).finish()
        return true
    } else {
        Toast.makeText(context, "Wrong Credentials", Toast.LENGTH_SHORT).show()
        return false
    }
}

@Composable
fun LoginField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Your email address",
    placeholder: String = "Enter your phone number",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    val focusManager = LocalFocusManager.current
    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Person,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(label)
        TextField(
            value = value,
            onValueChange = onChange,
            modifier = modifier,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = keyboardType
            ),
            keyboardActions = KeyboardActions (
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            placeholder = { Text(placeholder) },
            singleLine = true,
            visualTransformation = VisualTransformation.None
        )
    }
}

@Composable
fun PasswordField(
    value: String,
    onChange: (String) -> Unit,
    submit: () -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password",
    placeholder: String = "Enter your Password"
) {

    var isPasswordVisible by remember { mutableStateOf(false) }

    val leadingIcon = @Composable {
        Icon(
            Icons.Default.Key,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.primary
        )
    }
    val trailingIcon = @Composable {
        IconButton (onClick = { isPasswordVisible = !isPasswordVisible }) {
            Icon (
                if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }


    TextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = { submit() }
        ),
        placeholder = { Text(placeholder) },
        label = { Text(label) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation()
    )
}
