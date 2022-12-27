package ru.bondarenko.neurotest.neuro.mdp.binance.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.Trades
import java.util.*

@Repository
interface TradesRepo : CrudRepository<Trades, Long> {
    fun findFirstByTimeGreaterThanEqual(time: Long): Optional<Trades>
    fun findByTimeBetween(from: Long, to: Long): List<Trades>
}
