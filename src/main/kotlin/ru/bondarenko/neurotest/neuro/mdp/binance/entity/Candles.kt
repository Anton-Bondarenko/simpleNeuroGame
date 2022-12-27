package ru.bondarenko.neurotest.neuro.mdp.binance.entity

import javax.persistence.*

@Entity
@Table(name = "candles", schema = "BINANCE")
class Candles(
    @Id
    @Column(name = "ID")
    @GeneratedValue
    val id: Long? = 0,
    @Basic
    @Column(name = "openprice")
    val openPrice: Float = 0.0.toFloat(),
    @Basic
    @Column(name = "closeprice")
    val closePrice: Float = 0.0.toFloat(),
    @Basic
    @Column(name = "high")
    val high: Float = 0.0.toFloat(),
    @Basic
    @Column(name = "low")
    val low: Float = 0.0.toFloat(),
    @Basic
    @Column(name = "opentime")
    val openTime: Long = 0,
    @Basic
    @Column(name = "closetime")
    val closeTime: Long = 0,
    @Basic
    @Column(name = "tradecount")
    val tradeCount: Int = 0
)