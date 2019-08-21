package net.zomis.fights

import net.zomis.fight.ext.WinResult
import net.zomis.tttultimate.TTFactories
import net.zomis.tttultimate.TTPlayer
import net.zomis.tttultimate.games.TTClassicController
import net.zomis.tttultimate.players.TTAIFactory
import org.junit.jupiter.api.Test

class FightsTest {

    @Test
    fun fight() {
        val results = Fights()
            .between(TTAIFactory.idiot().build(), TTAIFactory.unreleased().build(), TTAIFactory.versionOne().build())
            .fight {
                val board = TTFactories().classicMNK(3)
                val game = TTClassicController(board)
                while (!game.isGameOver) {
                    val ai = if (game.currentPlayer.`is`(TTPlayer.X)) it.players[0] else it.players[1]
                    val move = ai.play(game) ?: break
                    game.play(move)
                }
                println("${game.wonBy} ${it.players}")
                it.gameResult(it.players[0], WinResult.result(game.wonBy == TTPlayer.NONE, game.wonBy == TTPlayer.X))
                it.gameResult(it.players[1], WinResult.result(game.wonBy == TTPlayer.NONE, game.wonBy == TTPlayer.O))
            }
            .fightEvenly(2)
        results.print()
    }

}