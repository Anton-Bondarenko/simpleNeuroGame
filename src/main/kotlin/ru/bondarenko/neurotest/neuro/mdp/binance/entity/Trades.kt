package ru.bondarenko.neurotest.neuro.mdp.binance.entity

import javax.persistence.*

@Entity
@Table(name = "trades", schema = "BINANCE")
class Trades(
    @Id
    @Column(name = "ID")
    val id: Long = 0,
    @Basic
    @Column(name = "price")
    val price: Double = 0.0,
    @Basic
    @Column(name = "time")
    val time: Long = 0,
    @Basic
    @Column(name = "quantity")
    val quantity: Int = 0
)