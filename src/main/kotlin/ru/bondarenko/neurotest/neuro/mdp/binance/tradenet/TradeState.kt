package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import org.deeplearning4j.rl4j.space.Encodable
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data.TradesAdapter
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.sink.SinkAction
import java.util.*

class TradeState(startTime: Long, private val dataProvider: TradesAdapter) : Encodable {
    var currentTime = startTime
    var sinkState: SinkAction = SinkAction.NO
    var data: Optional<TradesAdapter.Data> = Optional.empty()
    var currentData: Optional<TradesAdapter.Data> = Optional.empty()

    override fun toArray(): DoubleArray {
        var data = dataProvider.getPrepared(currentTime)
        if (data.isEmpty) data = this.data // no data, using previous
        this.data = data
        return if (data.isEmpty)
            doubleArrayOf(0.0, 0.0, 0.0, 0.0)
        else
            doubleArrayOf(
                data.get().time.toDouble(),
                data.get().quantity.toDouble(),
                data.get().price,
                data.get().priceDelta
            )

    }

}