import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Slot(ticTacToe: MutableState<TicTacToe>, index: Int) {
    val currentPlayer = ticTacToe.value.board[index]

    Box(
        modifier = Modifier
            .clickable(currentPlayer == null && ticTacToe.value.winner == null) {
                val new = ticTacToe.value.board.toMutableList()
                new[index] = ticTacToe.value.nextPlayer
                ticTacToe.value = ticTacToe.value.copy(
                    board = new,
                    nextPlayer = ticTacToe.value.nextPlayer.nextPlayer,
                    previousBoard = ticTacToe.value
                )
            }
            .border(1.dp, Color.Black)
            .size(40.dp)
    ) {
        val player = currentPlayer
        if (player != null) Text(text = player.label)
    }
}

@Composable
fun TicTacToeRow(ticTacToe: MutableState<TicTacToe>, startIndex: Int) {
    Row {
        Slot(ticTacToe, startIndex+0)
        Slot(ticTacToe, startIndex+1)
        Slot(ticTacToe, startIndex+2)
    }
}

@Composable
fun Screen() {
    val tictactoe = remember { mutableStateOf(TicTacToe()) }

    MaterialTheme {
        Column(Modifier.padding(10.dp)) {
            Row {
                Text("TicTacToe")

                if (tictactoe.value.previousBoard != null) {
                    Button(
                        onClick = {
                            tictactoe.value = tictactoe.value.previousBoard!!
                        }
                    ) {
                        Text("Undo")
                    }
                }
            }

            Column {
                TicTacToeRow(tictactoe, 0)
                TicTacToeRow(tictactoe, 3)
                TicTacToeRow(tictactoe, 6)
            }

            val winner = tictactoe.value.winner
            if (winner != null || tictactoe.value.full) {
                Row {

                    Button(onClick = {
                        tictactoe.value = TicTacToe()
                    }) {
                        Text(text = "Restart")
                    }

                    if (winner != null) Text("${winner.label} has won")
                    else Text("You're stuck")
                }


            }

        }
    }
}
