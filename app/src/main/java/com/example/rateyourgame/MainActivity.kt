package com.example.rateyourgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.room.Room
import com.example.rateyourgame.ViewModels.AuthViewModel
import com.example.rateyourgame.ViewModels.RatingViewModel
import com.example.rateyourgame.ViewModels.SharedViewModel
import com.example.rateyourgame.database.AppDatabase
import com.example.rateyourgame.screens.GameDetailsScreen
import com.example.rateyourgame.screens.GameListScreen
import com.example.rateyourgame.screens.LoginScreen
import com.example.rateyourgame.screens.SignUpScreen
import com.example.rateyourgame.screens.SplashScreen
import com.example.rateyourgame.ui.theme.RateYourGameTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app-database"
        ).build()

        val userDao = database.userDao()
        val authViewModel = AuthViewModel(userDao)
        val ratingDao = database.ratingDao()
        val ratingViewModel = RatingViewModel(ratingDao)

        setContent {
            RateYourGameTheme {
                val sharedViewModel: SharedViewModel = viewModel()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    MyApp(authViewModel, sharedViewModel, ratingViewModel)
                }
            }
        }
    }
}

@Composable
fun MyApp(authViewModel: AuthViewModel, sharedViewModel: SharedViewModel, ratingViewModel: RatingViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(navController = navController)
        }
        composable("login_screen") {
            LoginScreen(navController, authViewModel, sharedViewModel)
        }
        composable("signup_screen") {
            SignUpScreen(navController, authViewModel, sharedViewModel)
        }
        composable("game_list_screen") {
            GameListScreen(navController = navController, authViewModel)
        }
        composable("game_details_screen/{gameId}",
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getInt("gameId") ?: 0
            GameDetailsScreen(authViewModel, ratingViewModel, gameId = gameId)
        }
    }
}

