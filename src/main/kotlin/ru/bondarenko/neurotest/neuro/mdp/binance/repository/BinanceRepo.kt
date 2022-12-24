package ru.bondarenko.neurotest.neuro.mdp.binance.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.MarketEntity

@Repository
interface BinanceRepo: CrudRepository<MarketEntity, Long>
