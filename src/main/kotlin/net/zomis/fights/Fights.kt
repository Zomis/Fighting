package net.zomis.fights

import net.zomis.fight.ext.WinResult
import net.zomis.fight.ext.WinStats

class FightCallback<T>(val players: List<T>) {

    private val results: MutableMap<T, WinStats> = mutableMapOf()

    fun gameResult(player: T, winResult: WinResult) {
        results.putIfAbsent(player, WinStats(0, 0, 0))
        results[player]!!.add(winResult)
    }

    fun results(): Map<T, WinStats> {
        return results.toMap()
    }

//    fun save(result: R) {}

}

typealias FightInstruction<T> = (FightCallback<T>) -> Unit

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

    fun fightEvenly(count: Int): FightResult<T> {
        if (fightInstruction == null) {
            throw IllegalStateException("Need to call .fight first to setup how to perform a fight")
        }
        val callbacks = mutableListOf<FightCallback<T>>()
        for (first in who) {
            for (second in who) {
                if (first == second) {
                    continue
                }
                val players = listOf(first, second)
                val callback = FightCallback(players)
                repeat(count) {
                    fightInstruction!!(callback)
                }
                callbacks.add(callback)
            }
        }
        return FightResult(callbacks)
    }

}