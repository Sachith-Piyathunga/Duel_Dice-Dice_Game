
  // Import all necessary libraries for rub=n my code
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


/**
 * MainScreen Composable function that serves as the main menu of the application.
 * It provides two buttons: one for starting a new game and another for viewing information about the application.
 */

@Composable
fun MainScreen(
    onNewGame: () -> Unit,  // Callback for handling new game action
    onAbout: () -> Unit  // Callback for handling about action
) {
    // Column is used to arrange the UI elements vertically
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,  // Centers elements vertically
        horizontalAlignment = Alignment.CenterHorizontally  // Centers elements horizontally
    ) {
        // Add Button for starting a new game
        Button(
            onClick = onNewGame,  // Calls the onNewGame function when clicked
            modifier = Modifier.padding(8.dp)
        ) {
            Text("New Game")
        }
        // Add Button for navigating to the about section
        Button(
            onClick = onAbout,  // Calls the onAbout function when clicked
            modifier = Modifier.padding(8.dp)
        ) {
            Text("About")
        }
    }
}