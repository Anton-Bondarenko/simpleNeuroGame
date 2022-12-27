package ru.bondarenko.neurotest.neuro.mdp.windyplank

import org.deeplearning4j.rl4j.space.ObservationSpace
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class PlankObservationSpace(private val shape: IntArray,
                            private val low: INDArray = Nd4j.create(DoubleArray(1){-10.0}),
                            private val high: INDArray = Nd4j.create(DoubleArray(1){10.0})) : ObservationSpace<PlankState>{
    override fun getName(): String {
        return "TestEnvironment"
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