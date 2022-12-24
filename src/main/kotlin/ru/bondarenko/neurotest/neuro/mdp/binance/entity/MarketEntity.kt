package ru.bondarenko.neurotest.neuro.mdp.binance.entity

import javax.persistence.*

@Entity
@Table(name = "MARKET", schema = "BINANCE")
class MarketEntity(
    @Id
    @Column(name = "ID")
    val id: Long = 0,
    @Basic
    @Column(name = "PRICE")
    val price: Float? = null,
    @Basic
    @Column(name = "TIME")
    val time: Long? = null
)