package ru.bondarenko.neurotest.neuro.mdp.binance.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonSerialize

@JsonSerialize
data class TradesDTO(
    @JsonProperty("a") val id: Long,
    @JsonProperty("p") val price: String,
    @JsonProperty("q") val quantity: String,
    @JsonProperty("f") val firstId: String,
    @JsonProperty("l") val lastId: String,
    @JsonProperty("T") val time: Long,
    @JsonProperty("m") val isMaker: Boolean,
)