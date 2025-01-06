package com.cyberix.beac.frc.ui.start

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.cyberix.beac.frc.ui.start.login.LoginScreen
import com.cyberix.beac.frc.ui.start.otp.OTPVerificationScreen
import com.cyberix.beac.frc.ui.start.register.RegisterScreen

enum class StartScreens {
    WELCOME,
    LOGIN,
    OTP,
    REGISTER
}

sealed class StartNavigationItem(val route: String) {
    object Welcome: StartNavigationItem(StartScreens.WELCOME.name)
    object Login: StartNavigationItem(StartScreens.LOGIN.name)
    object Otp: StartNavigationItem(StartScreens.OTP.name)
    object Register: StartNavigationItem(StartScreens.REGISTER.name)
}

@Composable
fun StartNavHost(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = StartNavigationItem.Welcome.route
    ) {
        composable(route = StartNavigationItem.Welcome.route) {
            WelcomeScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(route = StartNavigationItem.Login.route) {
            LoginScreen(
                navController = navController,
                modifier = modifier
            )
        }
        composable(
            route = "${StartNavigationItem.Otp.route}/{encodedEmail}",
            arguments = listOf(navArgument("encodedEmail") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedEmail = backStackEntry.arguments?.getString("encodedEmail")
            val email = encodedEmail?.let { Uri.decode(it) }

            OTPVerificationScreen(
                navController = navController,
                email = email,
                modifier = modifier
            )
        }
        composable(route = StartNavigationItem.Register.route) {
            RegisterScreen(
                navController = navController,
                modifier = modifier
            )
        }
    }
}
