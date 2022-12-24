package ru.bondarenko.neurotest.neuro.mdp.binance.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "binance")
data class ApiConfig(
    var apiKey: String = "",
    var apiSecret: String = ""
)

