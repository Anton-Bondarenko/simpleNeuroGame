package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config.TradeNetSinkConfig
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter

class TradeEnvironment(val dataAdapter: TradesAdapter, val netConfig: TradeNetSinkConfig) {
    fun getDiff(fromTime: Long, period: Long): Double {
        val data = dataAdapter.getPrepared(fromTime, fromTime + period)
        if (data.isEmpty)
            return 0.0
        return data.get().priceDelta
    }
}