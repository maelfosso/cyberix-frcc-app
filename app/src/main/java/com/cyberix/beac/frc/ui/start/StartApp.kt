package com.cyberix.beac.frc.ui.start

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.cyberix.beac.frc.ui.start.confirm.ConfirmRegisterScreen
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

//    data class ConfirmRegistration(val token: String) : StartNavigationItem(
//        "register/confirm/{$token}"
//    )
    data class ConfirmRegistration(val token: String) : StartNavigationItem(
        "register/confirm/{token}"
    ) {
        companion object {
            const val route = "register/confirm/{token}"
            const val tokenArg = "token"
        }
    }
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
        composable(
            route = StartNavigationItem.ConfirmRegistration.route,
            arguments = listOf(navArgument(StartNavigationItem.ConfirmRegistration.tokenArg) { type = NavType.StringType }),
            deepLinks = listOf(
                navDeepLink { uriPattern = "https://yourdomain.com/register/confirm/{${StartNavigationItem.ConfirmRegistration.tokenArg}}" }
            )
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString(StartNavigationItem.ConfirmRegistration.tokenArg) ?: ""
            ConfirmRegisterScreen(
                navController = navController,
                token = token
            )
        }
    }
}
