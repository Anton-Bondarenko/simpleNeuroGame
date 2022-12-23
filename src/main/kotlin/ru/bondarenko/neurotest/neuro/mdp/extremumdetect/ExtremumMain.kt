package ru.bondarenko.neurotest.neuro.mdp.extremumdetect

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.BackpropType
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.LSTM
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.rl4j.learning.Learning
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense
import org.deeplearning4j.rl4j.network.dqn.IDQN
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.util.DataManager
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions
import ru.bondarenko.neurotest.neuro.mdp.DQNFactoryMultilayerDense
import ru.bondarenko.neurotest.neuro.mdp.windyplank.EnvironmentState
import ru.bondarenko.neurotest.neuro.mdp.windyplank.PlankMdp
import ru.bondarenko.neurotest.neuro.mdp.windyplank.PlankState
import ru.bondarenko.neurotest.neuro.mdp.windyplank.ShowMePlank

fun main() {
    ExtremumMain().runTest()
}

class ExtremumMain {

    private val testQl = QLearning.QLConfiguration(
        123,  //Random seed
        1000,  //Max step By epoch
        8000,  //Max step
        10000,  //Max size of experience replay
        32,  //size of batches
        100,  //target update (hard)
        0,  //num step noop warmup
        0.01,  //reward scaling
        0.99,  //gamma
        10.0,  //td-error clipping
        0.1f,  //min epsilon
        2000,  //num step for eps greedy anneal
        true //double DQN
    )

    //Set up network configuration:
    var conf = NeuralNetConfiguration.Builder()
        .seed(testQl.seed.toLong())
        .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
        .updater(Adam())
        .weightInit(WeightInit.XAVIER)
        .l2(0.001)
        .list()
        .layer(
            0, DenseLayer.Builder().nIn(1).nOut(10)
                .activation(Activation.RELU).build()
        )
        .layer(
            1, LSTM.Builder().nIn(10).nOut(10)
                .activation(Activation.TANH).build()
        )
        .layer(
            1, LSTM.Builder().nIn(10).nOut(10)
                .activation(Activation.TANH).build()
        )
        .layer(
            2, DenseLayer.Builder().nIn(10).nOut(10)
                .activation(Activation.RELU).build()
        )
        .layer(
            3, DenseLayer.Builder().nIn(10).nOut(10)
                .activation(Activation.RELU).build()
        )
        .layer(
            4,
            OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                .activation(Activation.IDENTITY) //MCXENT + softmax for classification
                .nIn(10).nOut(2).build()
        )
        .pretrain(false).backprop(true)
        .backpropType(BackpropType.Standard).tBPTTForwardLength(5).tBPTTBackwardLength(5)
        .build()

    fun runTest() {

        //record the training data in rl4j-data in a new folder
        val manager = DataManager()

        //define the mdp
        val mdp = PlankMdp()

        //define the training method
        val dql: Learning<PlankState, Int, DiscreteSpace, IDQN<*>> =
            QLearningDiscreteDense(mdp, DQNFactoryMultilayerDense(conf), testQl, manager)

        //start the training
        dql.train()
        manager.save(dql)
        mdp.close()
        checkDqn(dql)
    }

    private fun checkDqn(dql: Learning<PlankState, Int, DiscreteSpace, IDQN<*>>) {
        var plankState = PlankState()
        val environmentState = EnvironmentState()
        val visualizer = ShowMePlank(environmentState)
        do {
            environmentState.renderFrame(plankState)
            val input = dql.getInput(plankState)
            val action = dql.neuralNet.output(input)
            if (action.getDouble(0) > action.getDouble(1))
                plankState.dec()
            else
                plankState.inc()
            visualizer.update(plankState)
            if (environmentState.outOfRange(plankState))
                plankState = PlankState()
            Thread.sleep(1_00)
        } while (true)
    }
}