package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data

import java.util.*

interface DataAdapter<T, R> {
    fun getPrepared(input: T): Optional<R>
}