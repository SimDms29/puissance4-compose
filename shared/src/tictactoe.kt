
enum class Player {
    X, O;

    val label: String get() = when (this) {
        X -> "X"
        O -> "O"
    }

    val nextPlayer: Player get() = when (this) {
        X -> O
        O -> X
    }
}

val alignmentsToCheck: List<List<Int>> = buildList {
    // all lines
    listOf(0, 3, 6).forEach { add(listOf(it, it + 1, it + 2)) }
    // all columns
    listOf(0, 1, 2).forEach { add(listOf(it, it + 3, it + 6)) }
    // add diags
    add(listOf(0, 4, 8))
    add(listOf(2, 4, 6))
}

data class TicTacToe(
    val board: List<Player?> = List(9) { null },
    val nextPlayer: Player = Player.X,
    val previousBoard: TicTacToe? = null,
) {
    private fun checkAlignment(alignment: List<Int>): Player? {
        val first = board[alignment[0]]
        return if (first != null &&
            first == board[alignment[1]] &&
            first == board[alignment[2]]
        ) first else null
    }

    val full: Boolean = board.count { it == null } == 0

    val winner: Player? = alignmentsToCheck
        .firstNotNullOfOrNull { checkAlignment(it) }
}