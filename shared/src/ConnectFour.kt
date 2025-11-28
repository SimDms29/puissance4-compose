// ConnectFour.kt

package ConnectFour

enum class Player {
    Red, Yellow;

    val label: String get() = when (this) {
        Red -> "Rouge"
        Yellow -> "Jaune"
    }

    val nextPlayer: Player get() = when (this) {
        Red -> Yellow
        Yellow -> Red
    }
}

// Constantes du jeu standard
const val COLS = 7
const val ROWS = 6

data class ConnectFour(
    // Grille aplatie de 42 cases (6x7)
    val board: List<Player?> = List(COLS * ROWS) { null },
    val nextPlayer: Player = Player.Red,
    val previousBoard: ConnectFour? = null,
) {
    // Vérifie si la colonne est pleine
    fun canPlayColumn(col: Int): Boolean {
        return board[col] == null // Si la case tout en haut (index 0 à 6) est vide
    }

    // Logique de gravité : trouve la première case vide en partant du bas
    fun play(col: Int): ConnectFour {
        if (!canPlayColumn(col)) return this

        val newBoard = board.toMutableList()
        // On parcourt la colonne du bas (ROWS - 1) vers le haut (0)
        for (row in ROWS - 1 downTo 0) {
            val index = row * COLS + col
            if (newBoard[index] == null) {
                newBoard[index] = nextPlayer
                return this.copy(
                    board = newBoard,
                    nextPlayer = nextPlayer.nextPlayer,
                    previousBoard = this
                )
            }
        }
        return this
    }

    val full: Boolean = board.all { it != null }

    // Algorithme de détection de victoire dynamique (4 alignés)
    val winner: Player? by lazy {
        // Directions : Horizontale, Verticale, Diagonale Descendante, Diagonale Montante
        val directions = listOf(1, COLS, COLS + 1, COLS - 1)

        for (index in board.indices) {
            val player = board[index] ?: continue

            for (delta in directions) {
                if (checkLine(index, delta, player)) return@lazy player
            }
        }
        null
    }

    private fun checkLine(start: Int, delta: Int, player: Player): Boolean {
        // On vérifie si 4 pions sont alignés à partir de 'start' avec un pas de 'delta'
        // Attention aux limites de la grille (bords gauche/droite) pour éviter les sauts de ligne faux positifs
        for (i in 1..3) {
            val current = start + i * delta
            if (current < 0 || current >= board.size) return false

            // Vérification spécifique pour éviter que ça "traverse" les bords gauche/droite
            // Si on est en train de vérifier l'horizontal ou une diagonale
            val startCol = start % COLS
            val currentCol = current % COLS
            if (kotlin.math.abs(startCol - currentCol) > 3) return false // Trop loin horizontalement

            if (board[current] != player) return false
        }
        return true
    }
}