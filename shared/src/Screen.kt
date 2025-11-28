import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val Player.color: Color
    get() = when (this) {
        Player.X -> Color.Red
        Player.O -> Color.Blue
    }

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
            .size(100.dp)
    ) {
        AnimatedVisibility(
            visible = currentPlayer != null,
            enter = scaleIn(animationSpec = tween(durationMillis = 500)),
        ) {

            Canvas(
                modifier = Modifier.fillMaxSize()
                    .padding(8.dp)
            ) {
                when (currentPlayer) {
                    Player.X -> {
                        drawLine(
                            currentPlayer.color,
                            Offset(0f, 0f),
                            Offset(size.width, size.height),
                            strokeWidth = 3f
                        )
                        drawLine(
                            currentPlayer.color,
                            Offset(0f, size.height),
                            Offset(size.width, 0f),
                            strokeWidth = 3f
                        )
                    }

                    Player.O -> {
                        drawCircle(currentPlayer.color, style = Stroke(3f))
                    }
                    null -> {

                    }
                }
            }
        }
    }
}

@Composable
fun TicTacToeRow(ticTacToe: MutableState<TicTacToe>, startIndex: Int) {
    Row {
        Slot(ticTacToe, startIndex + 0)
        Slot(ticTacToe, startIndex + 1)
        Slot(ticTacToe, startIndex + 2)
    }
}

@Composable
fun Screen() {
    val tictactoe = remember { mutableStateOf(TicTacToe()) }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row {
                Text(
                    text = "TicTacToe",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Column {
                TicTacToeRow(tictactoe, 0)
                TicTacToeRow(tictactoe, 3)
                TicTacToeRow(tictactoe, 6)
            }

            val winner = tictactoe.value.winner
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                if (tictactoe.value.previousBoard != null) {
                    Button(
                        onClick = {
                            tictactoe.value = tictactoe.value.previousBoard!!
                        }
                    ) {
                        Text("Undo")
                    }
                }

                when {
                    winner != null -> {
                        Text(
                            text = "${winner.label} has won",
                            color = winner.color,
                            fontSize = 24.sp,
                        )
                    }

                    tictactoe.value.full -> {
                        Text(
                            text = "You're stuck",
                            fontSize = 24.sp,
                        )
                    }
                }


                if (winner != null || tictactoe.value.full) {
                    Button(onClick = {
                        tictactoe.value = TicTacToe()
                    }) {
                        Text(text = "Restart")
                    }
                }

            }

        }
    }
}
