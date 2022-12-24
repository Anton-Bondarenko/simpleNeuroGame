package ru.bondarenko.neurotest.neuro.mdp.binance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class AggTradesDTO(
    @JsonProperty("a") val id: Long,
    @JsonProperty("p") val price: String,
    @JsonProperty("q") val q: String,
    @JsonProperty("f") val f: Long,
    @JsonProperty("l") val l: Long,
    @JsonProperty("T") val time: Long,
    @JsonProperty("m") val isMaker: Boolean,
    @JsonProperty("M") val isBetMatch: Boolean
)