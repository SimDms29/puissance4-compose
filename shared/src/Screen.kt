package ConnectFour

// Screen.kt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Extension pour récupérer la couleur UI
val Player.color: Color
    get() = when (this) {
        Player.Red -> Color(0xFFD32F2F) // Rouge matériel
        Player.Yellow -> Color(0xFFFBC02D) // Jaune matériel
    }

@Composable
fun GridSlot(
    game: ConnectFour,
    index: Int,
    colIndex: Int,
    onPlay: (Int) -> Unit
) {
    val currentPlayer = game.board[index]

    Box(
        modifier = Modifier
            .size(50.dp) // Plus petit que le TicTacToe pour tenir sur l'écran
            .background(Color(0xFF1976D2)) // Fond bleu classique du Puissance 4
            .border(1.dp, Color(0xFF0D47A1))
            // Le clic sur n'importe quelle case de la colonne joue dans cette colonne
            .clickable(enabled = game.winner == null && !game.full) {
                onPlay(colIndex)
            }
            .padding(4.dp)
    ) {
        // Cercle vide (trou) ou plein (joueur)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Dessiner le "trou" en blanc si vide, ou la couleur du joueur
            drawCircle(
                color = currentPlayer?.color ?: Color.White,
            )
        }

        // Animation d'apparition du pion
        AnimatedVisibility(
            visible = currentPlayer != null,
            enter = scaleIn(animationSpec = tween(durationMillis = 300))
        ) {
            // Cette partie sert juste à déclencher l'anim, le dessin est géré au dessus
            // On pourrait affiner l'anim de chute ici plus tard
        }
    }
}

@Composable
fun Screen() {
    // État du jeu
    val gameState = remember { mutableStateOf(ConnectFour()) }
    val game = gameState.value

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Puissance 4",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            // Grille de jeu
            Column {
                for (row in 0 until ROWS) {
                    Row {
                        for (col in 0 until COLS) {
                            val index = row * COLS + col
                            GridSlot(
                                game = game,
                                index = index,
                                colIndex = col,
                                onPlay = { selectedCol ->
                                    gameState.value = game.play(selectedCol)
                                }
                            )
                        }
                    }
                }
            }

            // Interface de contrôle (Winner / Undo / Reset)
            val winner = game.winner
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Bouton Undo
                if (game.previousBoard != null && winner == null) {
                    Button(onClick = { gameState.value = game.previousBoard!! }) {
                        Text("Annuler")
                    }
                }

                // Affichage statut
                when {
                    winner != null -> {
                        Text(
                            text = "${winner.label} gagne !",
                            color = winner.color,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    game.full -> {
                        Text("Match nul !", fontSize = 24.sp)
                    }
                    else -> {
                        Text(
                            "Tour: ${game.nextPlayer.label}",
                            color = game.nextPlayer.color,
                            fontSize = 20.sp
                        )
                    }
                }

                // Bouton Restart
                if (winner != null || game.full) {
                    Button(onClick = { gameState.value = ConnectFour() }) {
                        Text("Rejouer")
                    }
                }
            }
        }
    }
}