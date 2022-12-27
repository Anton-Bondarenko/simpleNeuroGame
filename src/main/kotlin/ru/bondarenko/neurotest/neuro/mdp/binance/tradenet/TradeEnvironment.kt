package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter

class TradeEnvironment(val dataAdapter: TradesAdapter, val netConfig: TradeBaseNetConfig) {
    fun getDiff(currTime: Long, period: Long): Double{
        val entities = dataAdapter.getPrepared(currTime, currTime + period)
        if (entities.isEmpty)
            return 0.0
        return entities.get().priceDelta
    }
}