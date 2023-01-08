package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.data

import org.springframework.stereotype.Service
import ru.bondarenko.neurotest.neuro.mdp.binance.repository.TradesRepo
import java.util.*

@Service
class TradesAdapter(
    private val tradesRepo: TradesRepo
) : DataAdapter<Long, TradesAdapter.Data> {
    val period: Long = 1000
    override fun getPrepared(time: Long): Optional<Data> {
        return getPrepared(time-period, time)
    }

    fun getPrepared(from: Long, to: Long): Optional<Data> {
        val trades = tradesRepo.findByTimeBetween(from, to)
        if (trades.isEmpty())
            return Optional.empty()
        val prevTrade = tradesRepo.findById(trades.first().id - 1)
        if (prevTrade.isEmpty)
            return Optional.empty()
        var sum = 0.0
        var quantity = 0
        trades.forEach { trade ->
            sum += trade.price; quantity += trade.quantity
        }
        val midPrice = sum / trades.size
        return Optional.of(Data(midPrice, to, midPrice - prevTrade.get().price, quantity))
    }
    data class Data(
        val price: Double,
        val time: Long,
        val priceDelta: Double,
        val quantity: Int
    )
}