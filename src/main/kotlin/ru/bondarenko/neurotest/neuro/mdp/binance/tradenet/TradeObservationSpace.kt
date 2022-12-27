package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet

import org.deeplearning4j.rl4j.space.ObservationSpace
import org.nd4j.linalg.api.ndarray.INDArray

class TradeObservationSpace(
    private val shape: IntArray,
    private val low: INDArray,
    private val high: INDArray
) : ObservationSpace<TradeState> {

    override fun getName(): String {
        return "Trade Observation Space"
    }

    override fun getShape(): IntArray {
        return shape
    }

    override fun getLow(): INDArray {
        return low
    }

    override fun getHigh(): INDArray {
        return high
    }
}