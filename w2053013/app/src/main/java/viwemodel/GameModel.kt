package viwemodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameModel : ViewModel() {
    // Game State
    var playerDice by mutableStateOf(List(5) { 1 })
    var computerDice by mutableStateOf(List(5) { 1 })
    var humanScore by mutableStateOf(0)
    var computerScore by mutableStateOf(0)
    var rollsLeft by mutableStateOf(3)
    var selectedDice by mutableStateOf(setOf<Int>())
    var computerSelectedDice by mutableStateOf(setOf<Int>())
    var targetScore by mutableStateOf(101)
    var gameStatus by mutableStateOf(GameStatus.PLAYING)
    var humanWins by mutableStateOf(0)
    var computerWins by mutableStateOf(0)
    var isTieBreaker by mutableStateOf(false)
    var throwButtonEnabled by mutableStateOf(true)
    var scoreButtonEnabled by mutableStateOf(false)
    var isComputerRolling by mutableStateOf(false)

    enum class GameStatus {
        PLAYING, HUMAN_WON, COMPUTER_WON, TIE_BREAKER
    }

    init {
        resetGame()
    }

    fun resetGame() {
        playerDice = List(5) { 1 }
        computerDice = List(5) { 1 }
        humanScore = 0
        computerScore = 0
        rollsLeft = 3
        selectedDice = emptySet()
        computerSelectedDice = emptySet()
        gameStatus = GameStatus.PLAYING
        isTieBreaker = false
        throwButtonEnabled = true
        scoreButtonEnabled = false
    }

    fun rollDice(isHuman: Boolean) {
        if (isHuman) {
            handleHumanRoll()
        } else {
            viewModelScope.launch {
                handleComputerRoll()
            }
        }
    }

    private fun handleHumanRoll() {
        throwButtonEnabled = false
        viewModelScope.launch {
            playerDice = playerDice.mapIndexed { index, value ->
                if (index in selectedDice) value else Random.nextInt(1, 7)
            }
            delay(1000)

            rollsLeft--
            scoreButtonEnabled = true
            throwButtonEnabled = true

            if (rollsLeft == 0) {
                calculateScore()
            }
        }
    }

    private suspend fun handleComputerRoll() {
        isComputerRolling = true
        repeat(3) { rollCount ->
            computerDice = computerDice.mapIndexed { index, value ->
                if (shouldKeepComputerDie(index)) value else Random.nextInt(1, 7)
            }


            delay(2000)
        }
        isComputerRolling = false
    }

    private fun shouldKeepComputerDie(index : Int): Boolean {
        computerSelectedDice = getComputerKeepDice(computerDice)
        return computerSelectedDice.contains(index)
    }

    fun calculateScore() {
        viewModelScope.launch {
            handleComputerRoll()
            humanScore += playerDice.sum()
            computerScore += computerDice.sum()
            checkWinCondition()
            resetForNextRound()
        }
    }

    private fun checkWinCondition() {
        when {
            humanScore >= targetScore || computerScore >= targetScore -> handleWin()
            else -> gameStatus = GameStatus.PLAYING
        }
    }

    private fun handleWin() {
        gameStatus = when {
            humanScore >= targetScore && computerScore >= targetScore -> handleTie()
            humanScore >= targetScore -> handleHumanWin()
            else -> handleComputerWin()
        }
    }

    private fun handleHumanWin() : GameStatus{
        humanWins++
        return GameStatus.HUMAN_WON
    }

    private fun handleComputerWin() : GameStatus{
        computerWins++
        return GameStatus.COMPUTER_WON
    }

    private fun handleTie(): GameStatus {
        // Add tie-breaker logic here
        return GameStatus.HUMAN_WON // Simplified for example
    }

    private fun resetForNextRound() {
        rollsLeft = 3
        selectedDice = emptySet()
        computerSelectedDice = emptySet()
        playerDice = List(5) { 1 }
        computerDice = List(5) { 1 }
        throwButtonEnabled = true
        scoreButtonEnabled = false
    }

    /**
     * Learned about 'return when' syntax using the following url,
     *        https://stackoverflow.com/questions/48018091/kotlin-return-can-be-lifted-out-of-when
     * Refered to the code snippets given in the replies section.
     */
    private fun getComputerKeepDice(currentDice: List<Int>): Set<Int> {
        val remainingPoints = targetScore - computerScore
        val scoreDifference = humanScore - computerScore

        return when {
            // Final round - maximize single roll potential
            remainingPoints <= 30 && humanScore >= targetScore ->
                finalRoundStrategy(currentDice)

            // Conservative mode (close to target)
            remainingPoints <= 10 ->
                currentDice.filterIndices { it >= 4 }

            // Aggressive mode (significantly behind)
            scoreDifference > 20 ->
                aggressiveStrategy(currentDice, remainingPoints)

            // Default balanced strategy
            else ->
                currentDice.filterIndices { it >= 4 }
        }
    }

    private fun finalRoundStrategy(currentDice: List<Int>): Set<Int> {
        val currentSum = currentDice.sum()
        val potentialMax = currentSum + (5 - computerSelectedDice.size) * 6

        return if (potentialMax >= (targetScore - computerScore)) {
            // Go for maximum possible score
            currentDice.filterIndices { it >= 5 }
        } else {
            // Already can't win, keep safe values
            currentDice.filterIndices { it >= 4 }
        }
    }

    private fun aggressiveStrategy(
        currentDice: List<Int>,
        remaining: Int
    ): Set<Int> {
        return when {
            remaining > 30 ->
                currentDice.filterIndices { it >= 3 }
            remaining > 15 ->
                currentDice.filterIndices { it >= 4 }
            else ->
                currentDice.filterIndices { it >= 5 }
        }
    }

    /**
     * Lerned about helper extension functions using following blog article,
     *     https://www.dhiwise.com/post/kotlin-extension-function-enhancing-classes-streamlining-code
     * And the official documentation found in kotlin website,
     *     https://kotlinlang.org/docs/extensions.html#extensions-are-resolved-statically
     */
    private fun List<Int>.filterIndices(predicate: (Int) -> Boolean) =
        this.indices.filter { predicate(this[it]) }.toSet()

}