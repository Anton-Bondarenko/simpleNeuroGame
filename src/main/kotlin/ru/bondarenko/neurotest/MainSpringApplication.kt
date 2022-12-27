package ru.bondarenko.neurotest

import org.joda.time.format.ISODateTimeFormat
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import ru.bondarenko.neurotest.neuro.mdp.binance.binanceapi.BinanceService
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.sink.SinkTraining

@SpringBootApplication(scanBasePackages = ["ru.bondarenko.neurotest"])
open class MainSpringApplication(
    private val binanceService: BinanceService,
    private val sinkTraining: SinkTraining
) : CommandLineRunner {
    override fun run(vararg args: String?) {
//        getBinanceHistory()
        sinkTraining.run()
    }

    private fun getBinanceHistory() {
        binanceService.clearTrades()
        binanceService.getAndStoreTrades(
            ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2020-12-23T00:00:00Z").millis,
            ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2020-12-23T02:00:00Z").millis
        )
    }
}

fun main(args: Array<String>) {
    runApplication<MainSpringApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
        setHeadless(false)
    }
}