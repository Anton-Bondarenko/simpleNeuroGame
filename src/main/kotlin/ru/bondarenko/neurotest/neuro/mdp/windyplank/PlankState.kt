package ru.bondarenko.neurotest.neuro.mdp.windyplank

import org.deeplearning4j.rl4j.space.Encodable

class PlankState : Encodable{
    var step = 0L
    var x = 0.0

    override fun toArray(): DoubleArray {
        return DoubleArray(1) {x}
    }

    fun inc () {x++}
    fun dec () {x--}
}