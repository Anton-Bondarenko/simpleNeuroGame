package ru.bondarenko.neurotest.neuro.mdp

import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.deeplearning4j.rl4j.network.dqn.DQN
import org.deeplearning4j.rl4j.network.dqn.DQNFactory
import org.deeplearning4j.rl4j.network.dqn.IDQN
import org.deeplearning4j.rl4j.util.Constants

class DQNFactoryMultilayerDense(private val conf:MultiLayerConfiguration): DQNFactory {
    override fun buildDQN(shapeInputs: IntArray?, numOutputs: Int): IDQN<out IDQN<*>> {

        val model = MultiLayerNetwork(conf)
        model.init()
        model.setListeners(ScoreIterationListener(Constants.NEURAL_NET_ITERATION_LISTENER))

        return DQN(model)
    }
}