package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.BackpropType
import org.deeplearning4j.nn.conf.InputPreProcessor
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.inputs.InputType
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions


class TradeNetSinkConfig {
    val netWide = 10
    val period: Long = 1000
    val numFeatures: Int = 1
    val featuresShape: IntArray = intArrayOf(numFeatures)
    val numActions: Int = 2
    val actionSpace: DiscreteSpace = DiscreteSpace(numActions)
    val testQl = QLearning.QLConfiguration(
        123456,  //Random seed
        1000,  //Max step By epoch
        500000,  //Max step
        1000,  //Max size of experience replay
        32,  //size of batches
        100,  //target update (hard)
        10,  //num step noop warmup
        0.01,  //reward scaling
        0.99,  //gamma
        10.0,  //td-error clipping
        0.1F,  //min epsilon
        2000,  //num step for eps greedy anneal
        true //double DQN
    )
    val preProcessorMap: MutableMap<Int, InputPreProcessor> = object : HashMap<Int, InputPreProcessor>() {
        init {
//            put(1, FeedForwardToRnnPreProcessor())
//            put(2, RnnToFeedForwardPreProcessor())
        }
    }

    //Set up network configuration:
    val conf: MultiLayerConfiguration = run {
        var ind = 0
        NeuralNetConfiguration.Builder()
            .seed(testQl.seed.toLong())
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .updater(Nesterovs(0.01, 0.99))
            .weightInit(WeightInit.XAVIER)
            .activation(Activation.SOFTMAX)
            .l2(0.01)
            .list()
//            .layer(
//                ind++, LSTM.Builder().nIn(numFeatures).nOut(netWide)
//                    .activation(Activation.RELU).name("rrn_layer").build()
//            )
            .layer(
                ind++, DenseLayer.Builder().nIn(numFeatures).nOut(netWide).hasBias(true).name("dense_layer_1")
                    .activation(Activation.RELU).build()
            )
            .layer(
                ind++, DenseLayer.Builder().nIn(netWide).nOut(netWide).hasBias(true).name("dense_layer_2")
                    .activation(Activation.RELU).build()
            )
            .layer(
                ind++, DenseLayer.Builder().nIn(netWide).nOut(netWide).hasBias(true).name("dense_layer_3").build()
            )
            .layer(
                ind++, OutputLayer.Builder(LossFunctions.LossFunction.MCXENT).nIn(netWide).nOut(numActions).build()
            )
            .pretrain(false).backprop(true)
            .setInputType(InputType.feedForward(numFeatures.toLong()))
            .backpropType(BackpropType.Standard)
//            .inputPreProcessors(preProcessorMap)
            .build()
    }
}

enum class TradeAction(val value: Int) {
    UP(0), DOWN(1);
}