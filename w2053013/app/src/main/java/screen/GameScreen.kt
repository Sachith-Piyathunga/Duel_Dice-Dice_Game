
// Import all necessary libraries for rub=n my code
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dicegame.R
import viwemodel.GameModel

/**
 * Learned how to declare ViewModel the following link
 * 	https://developer.android.com/topic/libraries/architecture/viewmodel
 * Refered to the given example stetments
 *
 *  * This GameScreen Composable represents the main gameplay screen for the dice game.
 *  * It displays the player's and the computer's dice, allows interactions, and manages game states.
 *  */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Duel Dice") },  // Displays the game title
                actions = {
                    // Displays the number of wins for both human and computer
                    Text("H:${viewModel.humanWins}/C:${viewModel.computerWins}", Modifier.padding(end = 16.dp))
                    // Displays the current score of the game
                    Text("${viewModel.humanScore} - ${viewModel.computerScore}")
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            /// Display Human Player's Dice
            Text("Your Dice")
            DiceRow(
                dice = viewModel.playerDice,
                selected = viewModel.selectedDice,
                onDiceSelected = { index ->
                    viewModel.selectedDice = viewModel.selectedDice.toMutableSet().apply {
                        if (contains(index)) remove(index) else add(index)
                    }
                }
            )

            // Display Computer's Dice
            Text("Computer Dice")
            DiceRow(
                dice = viewModel.computerDice,
                selected = viewModel.computerSelectedDice,
                onDiceSelected = { index ->
                    viewModel.computerSelectedDice = viewModel.computerSelectedDice.toMutableSet().apply {
                        if (contains(index)) remove(index) else add(index)
                    }
                }
            )

            Spacer(Modifier.weight(1f))  // Pushes the buttons to the bottom

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Button for rolling the dice
                Button(
                    onClick = { viewModel.rollDice(isHuman = true) },
                    enabled = viewModel.throwButtonEnabled && !viewModel.isComputerRolling
                ) {
                    Text("Throw (${viewModel.rollsLeft} left)")
                }

                // Button for calculate the score
                Button(
                    onClick = { viewModel.calculateScore() },
                    enabled = viewModel.scoreButtonEnabled && !viewModel.isComputerRolling
                ) {
                    Text("Score")
                }
            }
        }
    }

    // Win Dialog
    when (viewModel.gameStatus) {
        GameModel.GameStatus.HUMAN_WON -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetGame(); onBack() },
                title = { Text("You Win!") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.resetGame()
                        onBack()
                    }) {
                        Text("OK")
                    }
                },
                containerColor = Color.Green.copy(alpha = 0.8f)
            )
        }
        GameModel.GameStatus.COMPUTER_WON -> {
            AlertDialog(
                onDismissRequest = { viewModel.resetGame(); onBack() },
                title = { Text("Game Over") },
                text = { Text("Computer Wins!") },
                confirmButton = {
                    Button(onClick = {
                        viewModel.resetGame()
                        onBack()
                    }) {
                        Text("OK")
                    }
                },
                containerColor = Color.Red.copy(alpha = 0.8f)
            )
        }
        else -> {}
    }

    RollingDiceAnimation(viewModel)
}

@Composable
private fun RollingDiceAnimation(viewModel: GameModel) {
    if (viewModel.isComputerRolling) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "Computer is rolling...",
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                color = Color.Red
            )
        }
    }
}

@Composable
fun DiceRow(
    dice: List<Int>,
    selected: Set<Int> = emptySet(),
    onDiceSelected: (Int) -> Unit = {}
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dice.size) { index ->
            val diceValue = dice[index]
            val isSelected = index in selected
            Box(
                modifier = Modifier
                    .border(
                        width = if (isSelected) 2.dp else 0.dp,
                        color = if (isSelected) Color.Blue else Color.Transparent
                    )
                    .clickable { onDiceSelected(index) }
            ) {
                Image(
                    painter = painterResource(id = getDiceResource(diceValue)),
                    contentDescription = "Dice $diceValue",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    }
}

// Add this in a separate file or at the bottom
@Composable
fun getDiceResource(value: Int): Int {
    return when (value) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
}