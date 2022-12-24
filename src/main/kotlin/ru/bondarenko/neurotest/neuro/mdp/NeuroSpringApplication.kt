package ru.bondarenko.neurotest.neuro.mdp

import org.joda.time.format.ISODateTimeFormat
import org.springframework.boot.Banner
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import ru.bondarenko.neurotest.neuro.mdp.binance.BinanceService

@SpringBootApplication(scanBasePackages= ["ru.bondarenko.neurotest.neuro"])
@EnableJpaRepositories(basePackages = ["ru.bondarenko.neurotest.neuro"])
open class NeuroSpringApplication(private val binanceService: BinanceService): CommandLineRunner {
    override fun run(vararg args: String?) {
        binanceService.getAndStoreHistory(
            ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2020-12-23T00:00:00Z").millis,
            ISODateTimeFormat.dateTimeNoMillis().parseDateTime("2020-12-24T00:00:00Z").millis)
    }
}
fun main(args: Array<String>) {
    runApplication<NeuroSpringApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}