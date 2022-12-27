package ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.sink

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.BackpropType
import org.deeplearning4j.nn.conf.MultiLayerConfiguration
import org.deeplearning4j.nn.conf.NeuralNetConfiguration
import org.deeplearning4j.nn.conf.layers.DenseLayer
import org.deeplearning4j.nn.conf.layers.LSTM
import org.deeplearning4j.nn.conf.layers.OutputLayer
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.nd4j.linalg.activations.Activation
import org.nd4j.linalg.learning.config.Adam
import org.nd4j.linalg.lossfunctions.LossFunctions
import ru.bondarenko.neurotest.neuro.mdp.binance.tradenet.TradeBaseNetConfig

class TradeNetSinkConfig : TradeBaseNetConfig() {
    val netWide = 10
    val ltmLayersAmount = 1
    val numActions: Int = 2
    val predictionTime: Long = 200 // planning horizon
    val actionSpace: DiscreteSpace = DiscreteSpace(numActions)
    val testQl = QLearning.QLConfiguration(
        123,  //Random seed
        10000,  //Max step By epoch
        100000,  //Max step
        10000,  //Max size of experience replay
        32,  //size of batches
        100,  //target update (hard)
        0,  //num step noop warmup
        0.01,  //reward scaling
        0.99,  //gamma
        10.0,  //td-error clipping
        0.1f,  //min epsilon
        5000,  //num step for eps greedy anneal
        true //double DQN
    )

    //Set up network configuration:
    val conf: MultiLayerConfiguration = run {
        var ind = 0
        val layerListBuilder =
            NeuralNetConfiguration.Builder()
                .seed(testQl.seed.toLong())
                .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
                .updater(Adam())
                .weightInit(WeightInit.XAVIER)
                .l2(0.001)
                .list()
                .layer(
                    ind++, DenseLayer.Builder().nIn(numFeatures).nOut(10)
                        .activation(Activation.RELU).build()
                )
        for (i in ind..ind + ltmLayersAmount) {
            layerListBuilder.layer(
                i, LSTM.Builder().nIn(netWide).nOut(netWide)
                    .activation(Activation.TANH).build()
            )
            ind++
        }
        //MCXENT + softmax for classification
        layerListBuilder.layer(
            ind++, DenseLayer.Builder().nIn(netWide).nOut(netWide)
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
                    .nIn(10).nOut(numActions).build()
            )
            .pretrain(false).backprop(true)
            .backpropType(BackpropType.Standard).tBPTTForwardLength(5).tBPTTBackwardLength(5)
            .build()
    }
}
enum class SinkAction(val value: Int) {
    NO(0), YES(1);
}