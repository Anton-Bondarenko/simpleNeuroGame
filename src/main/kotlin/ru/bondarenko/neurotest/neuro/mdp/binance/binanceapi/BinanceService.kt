package ru.bondarenko.neurotest.neuro.mdp.binance.binanceapi

import com.binance.connector.client.impl.CMFuturesClientImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.bondarenko.neurotest.neuro.mdp.binance.config.ApiConfig
import ru.bondarenko.neurotest.neuro.mdp.binance.dto.CandlesDTO
import ru.bondarenko.neurotest.neuro.mdp.binance.dto.TradesDTO
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.Candles
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.Trades
import ru.bondarenko.neurotest.neuro.mdp.binance.repository.CandlesRepo
import ru.bondarenko.neurotest.neuro.mdp.binance.repository.TradesRepo


@Service
class BinanceService(
    apiConf: ApiConfig,
    private val candlesRepo: CandlesRepo,
    private val tradesRepo: TradesRepo
) {

    private val client = CMFuturesClientImpl(apiConf.apiKey, apiConf.apiSecret)
    fun getAndStoreCandlesHistory(from: Long, to: Long) {
        val maxPackageLength = 1500
        var currentFrom = from
        val parameters = LinkedHashMap<String, Any>()
        parameters["symbol"] = "BTCUSD_PERP"
        parameters["interval"] = "1m"
        parameters["limit"] = maxPackageLength
        parameters["endTime"] = to.toString()
        do {
            parameters["startTime"] = currentFrom.toString()
            val response = client.market().klines(parameters)
            val objectMapper = ObjectMapper()
            val aggTradesDto = objectMapper.readValue(response, object : TypeReference<List<CandlesDTO>>() {})
            val marketEntities =
                aggTradesDto.stream().map { trade ->
                    Candles(
                        null,
                        trade.open.toFloat(),
                        trade.close.toFloat(),
                        trade.high.toFloat(),
                        trade.low.toFloat(),
                        trade.openTime,
                        trade.closeTime,
                        trade.tradeCount
                    )
                }.toList()
            candlesRepo.saveAll(marketEntities)
            if (aggTradesDto.isNotEmpty()) currentFrom = aggTradesDto.last().closeTime
        } while (aggTradesDto.isNotEmpty() && (currentFrom < to))
    }

    fun getAndStoreTrades(from: Long, to: Long) {
        val maxPackageLength = 1000
        var currentFrom = from
        val parameters = LinkedHashMap<String, Any>()
        parameters["symbol"] = "BTCUSD_PERP"
        parameters["limit"] = maxPackageLength
        parameters["endTime"] = to.toString()
        do {
            parameters["startTime"] = currentFrom.toString()
            val response = client.market().aggTrades(parameters)
            val objectMapper = ObjectMapper()
            val aggTradesDto = objectMapper.readValue(response, object : TypeReference<List<TradesDTO>>() {})
            val marketEntities =
                aggTradesDto.stream().map { trade ->
                    Trades(
                        trade.id,
                        trade.price.toDouble(),
                        trade.time,
                        trade.quantity.toInt()
                    )
                }.toList()
            tradesRepo.saveAll(marketEntities)
            if (aggTradesDto.isNotEmpty()) currentFrom = aggTradesDto.last().time
        } while (aggTradesDto.isNotEmpty() && (currentFrom < to))
    }

    fun clearCandles() {
        candlesRepo.deleteAll()
    }
    fun clearTrades() {
        tradesRepo.deleteAll()
    }
}