package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import org.deeplearning4j.rl4j.space.Encodable
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config.TradeAction
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config.TradeNetSinkConfig
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter
import java.util.*

class TradeState(startTime: Long, private val dataProvider: TradesAdapter) : Encodable{
    var currentTime = startTime
    var tradeAction: TradeAction = TradeAction.UP
    var data: Optional<TradesAdapter.Data> = Optional.empty()
    var lastReward: Double = 0.0
    val config = TradeNetSinkConfig()

    override fun toArray(): DoubleArray {
        var data = dataProvider.getPrepared(currentTime)
        if (data.isEmpty) data = this.data // no data, using previous
        this.data = data
        val resArr = if (data.isEmpty)
            doubleArrayOf(0.0)
        else
            doubleArrayOf(
                data.get().priceDelta
            )
        if (config.numFeatures != resArr.size)
            throw IllegalArgumentException("Features num does not fit actual data size (wrong setup)")
        return resArr
    }

}