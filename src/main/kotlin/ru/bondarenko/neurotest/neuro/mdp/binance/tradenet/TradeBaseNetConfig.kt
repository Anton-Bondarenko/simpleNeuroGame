package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

/**
 * feature 1 = time
 * feature 2 = price
 *
 * action 1 = no
 * action 2 = yes
 */
open class TradeBaseNetConfig (
    private val dataConfig: DataBaseConfig = DataBaseConfig(),
    val numFeatures: Int = dataConfig.inputNum,
    val featuresShape: IntArray = intArrayOf(numFeatures),
    val normalizationLow: INDArray = Nd4j.create(doubleArrayOf(1607000000000.0, 10000.0)),
    val normalizationHigh: INDArray = Nd4j.create(doubleArrayOf(1609000000000.0, 30000.0))
)

