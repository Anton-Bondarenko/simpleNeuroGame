package ru.bondarenko.neurotest.neuro.mdp.binance.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.bondarenko.neurotest.neuro.mdp.binance.entity.Candles

@Repository
interface CandlesRepo: CrudRepository<Candles, Long>{
    fun findFirstByOpenTimeGreaterThanEqual(time: Long): Candles
    fun findByOpenTimeBetween(from: Long, to: Long): List<Candles>

}
