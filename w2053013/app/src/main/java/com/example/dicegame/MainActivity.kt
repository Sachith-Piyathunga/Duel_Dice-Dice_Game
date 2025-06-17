package com.example.dicegame

// Import all necessary libraries for rub=n my code
import viwemodel.GameModel
import GameScreen
import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * MainActivity is the entry point of the Dice Game application.
 * It sets up the UI using Jetpack Compose and initializes the game structure.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceGameApp()  // Calls the main composable function to start the app
        }
    }
}

/**
 * Learned how to declare remember the following link
 * 	https://developer.android.com/develop/ui/compose/state#state-in-compose
 * Refered to the given exmple stetments
 * Duel Dice Game App is the main composable function that manages the navigation and app structure.
 */
@Composable
fun DiceGameApp() {
    val navController = rememberNavController()
    val viewModel: GameModel = viewModel()
    val openDialog = remember { mutableStateOf(false) }

    // Navigation host to manage different screens in the app
    NavHost(navController = navController, startDestination = "main") {
        // Main screen with navigation options
        composable("main") {
            MainScreen(
                onNewGame = { navController.navigate("game") },
                onAbout = { openDialog.value = true}
            )
        }
        // Game screen where the dice game logic is handled
        composable("game") {
            GameScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }

    // Displays the dialog if openDialog is set to true
    if (openDialog.value) {
        MinimalDialog { openDialog.value = false }
    }
}

/**
 * Lerned about Dialog component from the following url,
 *     https://developer.android.com/develop/ui/compose/components/dialog
 * Reffered to the examples given in the page
 */
@Composable
fun MinimalDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)  // Sets a fixed height for the dialog
                .padding(16.dp),  // Adds padding around the card
            shape = RoundedCornerShape(16.dp),  // Rounds the corners of the dialog
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Studen Name: Sachintha Chamod Piyatunga\nStuden ID: 20530137",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Displays the plagiarism declaration statement
                Text(
                    text = "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged.",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(onClick = { onDismissRequest() }) {
                    Text(text = "OK")
                }
            }
        }
    }
}