package ru.bondarenko.neurotest.neuro.mdp.test

import org.deeplearning4j.rl4j.space.ObservationSpace
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class TestObservationSpace: ObservationSpace<TestState>{
    private var shape: IntArray
    private var low: INDArray
    private var high: INDArray

    constructor(shape: IntArray) {
        this.shape = shape
        low = Nd4j.create(1)
        high = Nd4j.create(1)
    }

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