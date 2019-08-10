package net.zomis.fights

import net.zomis.fight.ext.WinResult

class FightCallback<T>(val players: List<T>) {

    private val results: MutableMap<T> = mutableMapOf<>()

    fun gameResult(player: T, winResult: WinResult) {

    }

}

typealias FightInstruction<T> = FightCallback<T>.(List<T>) -> Unit

class Fights {

    fun <T : Any> between(vararg who: T): Fight<T> {
        return Fight(who.toList())
    }

}

class Fight<T : Any>(val who: List<T>) {

    private var fightInstruction: FightInstruction<T>? = null

    fun fight(how: FightInstruction<T>): Fight<T> {
        this.fightInstruction = how
        return this
    }

    fun fightEvenly(count: Int): FightResult {
        val callbacks = mutableListOf<FightCallback<T>>()
        for (first in who) {
            for (second in who) {
                if (first == second) {
                    continue
                }
                val players = listOf(first, second)
                val callback = FightCallback(players)
                repeat(count) {
                    fightInstruction!!(callback, players)
                }
                callbacks.add(callback)
            }
        }
        return FightResult(callbacks)
    }

}