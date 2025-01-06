package com.cyberix.beac.frc.ui.start.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cyberix.beac.frc.R
import com.cyberix.beac.frc.data.network.ApiState
import com.cyberix.beac.frc.ui.start.StartNavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen (
    navController: NavController,
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = hiltViewModel()
) {
    val registerUIState by registerViewModel.uiState.collectAsState()
    val requestState by registerViewModel.requestState.collectAsState()

    val context = LocalContext.current

    var showRegistrationSuccessDialog by remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .padding(vertical = 16.dp),
                navigationIcon = {
                    Button (
                        onClick = {
                            navController.navigateUp()
                        }
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

        Column (
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
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
                    text = "Enregistrement",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            RegisterForm(
                registerUIState,
                onChanged = { newState ->
                    registerViewModel.updateForm(newState)
                },
                onSubmit = { registerViewModel.submit() }
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
                showRegistrationSuccessDialog = true
                RegistrationConfirmationDialog(
                    showDialog = showRegistrationSuccessDialog
                ) {
                    navController.navigate(StartNavigationItem.Welcome.route)
                    showRegistrationSuccessDialog = false
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
fun RegisterForm(
    registerUIState: RegisterUIState,
    onChanged: (RegisterUIState) -> Unit,
    onSubmit: () -> Unit,
) {
    Box {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            RegisterField(
                value = registerUIState.firstName,
                label = "Nom",
                placeholder = "Entrez votre nom",
                onChange = { value -> onChanged(
                    registerUIState.copy(firstName = value)
                )},
                modifier = Modifier.fillMaxWidth()
            )
            RegisterField(
                value = registerUIState.lastName,
                label = "Prenom",
                placeholder = "Entrez votre prenom",
                onChange = { value -> onChanged(
                    registerUIState.copy(lastName = value)
                )},
                modifier = Modifier.fillMaxWidth()
            )
            RegisterField(
                value = registerUIState.email,
                label = "Email",
                placeholder = "Entrez votre address email",
                onChange = { value -> onChanged(
                    registerUIState.copy(email = value)
                )},
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )
            RegisterChoice(
                value = registerUIState.quality,
                choices = listOf(Quality.Expert.name, Quality.Participant.name),
                label = "Qualité",
                onChange = { value -> onChanged(
                    registerUIState.copy(quality = value)
                )},
                modifier = Modifier.fillMaxWidth()
            )
            RegisterField(
                value = registerUIState.phone,
                label = "Telephone",
                placeholder = "Entrez votre numéro de téléphone",
                onChange = { value -> onChanged(
                    registerUIState.copy(phone = value)
                )},
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Phone
            )
            RegisterField(
                value = registerUIState.organization,
                label = "Organisation",
                placeholder = "Entrez le nom de votre organization",
                onChange = { value -> onChanged(
                    registerUIState.copy(organization = value)
                )},
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    onSubmit()
                },
                enabled = registerUIState.isNotEmpty(),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enregistrez-vous")
            }
        }
    }
}

@Composable
fun RegisterField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Phone number",
    placeholder: String = "Enter your phone number",
    keyboardType: KeyboardType = KeyboardType.Text
) {

    val focusManager = LocalFocusManager.current

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
fun RegisterChoice(
    value: String,
    choices: List<String>,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Qualité",
    placeholder: String = "Faites un choix"
) {
    Column {
        Text(label)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            choices.map { choice ->
                FilterChip(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    selected = value == choice,
                    onClick = { onChange(choice) },
                    label = { Text(text = choice) },
                    leadingIcon = if (value == choice) {
                        {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "Selected"
                            )
                        }
                    } else {
                        null
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationConfirmationDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissRequest) {
            Surface(
                modifier = Modifier
                    .width(300.dp)
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Success Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Enregistrement réussi!",
                        style = MaterialTheme.typography.headlineSmall,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "S'il vous plait, vérifiez votre adresse email pour confirmer votre enregistrement",
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "OK"
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegistrationConfirmationDialog() {
    MaterialTheme {
        var showDialog by remember { mutableStateOf(true) }
        RegistrationConfirmationDialog(showDialog = showDialog) {
            showDialog = false
        }
    }
}
//
//
//@Composable
//fun MyScreen() {
//    var showDialog by remember { mutableStateOf(false) }
//    val context = LocalContext.current
//    Column {
//        Button(onClick = { showDialog = true }) {
//            Text("Register")
//        }
//        RegistrationConfirmationDialog(showDialog = showDialog) {
//            showDialog = false
//        }
//    }
//}
//
//@Preview
//@Composable
//fun PreviewMyScreen() {
//    MyScreen()
//}