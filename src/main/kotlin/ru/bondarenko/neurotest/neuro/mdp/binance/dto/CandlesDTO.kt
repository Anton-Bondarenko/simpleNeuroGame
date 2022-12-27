package ru.bondarenko.neurotest.neuro.mdp.binance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class CandlesDTO(
    @JsonProperty("open") val open: String,
    @JsonProperty("high") val high: String,
    @JsonProperty("low") val low: String,
    @JsonProperty("close") val close: String,
    @JsonProperty("volume") val volume: String,
    @JsonProperty("amount") val amount: String,
    @JsonProperty("interval") val interval: String,
    @JsonProperty("tradeCount") val tradeCount: Int,
    @JsonProperty("takerVolume") val takerVolume: String,
    @JsonProperty("takerAmount") val takerAmount: String,
    @JsonProperty("openTime") val openTime: Long,
    @JsonProperty("closeTime") val closeTime: Long
)