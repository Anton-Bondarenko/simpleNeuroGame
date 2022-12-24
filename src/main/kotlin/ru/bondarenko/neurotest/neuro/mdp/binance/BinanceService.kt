package ru.bondarenko.neurotest.neuro.mdp.binance

import com.binance.connector.client.impl.CMFuturesClientImpl
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import ru.bondarenko.neurotest.neuro.mdp.binance.config.ApiConfig
import ru.bondarenko.neurotest.neuro.mdp.binance.dto.AggTradesDTO
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.MarketEntity
import ru.bondarenko.neurotest.neuro.mdp.binance.repository.BinanceRepo


@Service
class BinanceService(
    apiConf: ApiConfig,
    private val binanceRepo: BinanceRepo
) {
    private val maxPackageLength = 1000
    private val client = CMFuturesClientImpl(apiConf.apiKey, apiConf.apiSecret)
    fun getAndStoreHistory(from: Long, to: Long) {
        var currentFrom = from
        val parameters = LinkedHashMap<String, Any>()
        parameters["symbol"] = "BTCUSD_PERP"
        parameters["limit"] = maxPackageLength
        parameters["endTime"] = to.toString()
        do {
            parameters["startTime"] = currentFrom.toString()
            val response = client.market().aggTrades(parameters)
            val objectMapper = ObjectMapper()
            val aggTradesDto = objectMapper.readValue(response, object : TypeReference<List<AggTradesDTO>>() {})
            val marketEntities =
                aggTradesDto.stream().map { trade -> MarketEntity(trade.id, trade.price.toFloat(), trade.time) }.toList()
            binanceRepo.saveAll(marketEntities)
            if (aggTradesDto.isNotEmpty()) currentFrom = aggTradesDto.last().time + 1
        } while (aggTradesDto.isNotEmpty() || (currentFrom < to))
    }

    fun clear(){
        binanceRepo.deleteAll()
    }
}