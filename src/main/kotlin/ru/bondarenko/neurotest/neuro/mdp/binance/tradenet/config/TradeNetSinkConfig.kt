package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.config

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.BackpropType
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.BatchNormalization
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Nesterovs
import org.nd4j.linalg.lossfunctions.LossFunctions

class TradeNetSinkConfig : TradeBaseNetConfig() {
    val netWide = 20

    val numActions: Int = 2
    val actionSpace: DiscreteSpace = DiscreteSpace(numActions)
    val testQl = QLearning.QLConfiguration(
        123,  //Random seed
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
        5000,  //num step for eps greedy anneal
        true //double DQN
    )

    val normalizationLayer =  BatchNormalization.Builder().nIn(numFeatures).nOut(numFeatures)
        .activation(Activation.SOFTMAX).build()
    //Set up network configuration:
    val conf: MultiLayerConfiguration = run {
        var ind = 0
        NeuralNetConfiguration.Builder()
            .seed(testQl.seed.toLong())
            .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
            .updater(Nesterovs(0.001, 0.7))
            .weightInit(WeightInit.XAVIER)
            .l2(0.0005)
            .list()
            .layer(
                ind++,  normalizationLayer)
            .layer(
                ind++,  DenseLayer.Builder().nIn(numFeatures).nOut(netWide)
                    .activation(Activation.RELU).build()
            )
            .layer(
                ind++, DenseLayer.Builder().nIn(netWide).nOut(netWide)
                    .activation(Activation.RELU).build()
            )
            .layer(
                ind++,
                OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                    .activation(Activation.IDENTITY) //MCXENT + softmax for classification
                    .nIn(netWide).nOut(numActions).build()
            )
            .pretrain(false).backprop(true)
            .backpropType(BackpropType.Standard).tBPTTForwardLength(5).tBPTTBackwardLength(5)
            .build()
    }
}

enum class SinkAction(val value: Int) {
    NO(0), YES(1);
}