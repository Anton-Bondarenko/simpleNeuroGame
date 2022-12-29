package ru.bondarenko.neurotest.neuro.mdp.windyplank

import org.deeplearning4j.rl4j.learning.Learning
import org.deeplearning4j.rl4j.learning.sync.qlearning.QLearning.QLConfiguration
import org.deeplearning4j.rl4j.learning.sync.qlearning.discrete.QLearningDiscreteDense
import org.deeplearning4j.rl4j.network.dqn.DQNFactoryStdDense
import org.deeplearning4j.rl4j.network.dqn.IDQN
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.util.DataManager
import org.nd4j.linalg.learning.config.Adam

fun main() {
    TestMain().runTest()
}

class TestMain {
    private val testQl = QLConfiguration(
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

    private var testNet = DQNFactoryStdDense.Configuration.builder()
        .l2(0.01).updater(Adam(1e-2)).numLayer(3).numHiddenNodes(16).build()


    fun runTest() {

        //record the training data in rl4j-data in a new folder
        val manager = DataManager()

        //define the mdp
        val mdp = PlankMdp()

//        define the training method
        val dql: Learning<PlankState, Int, DiscreteSpace, IDQN<*>> =
            QLearningDiscreteDense(mdp, testNet, testQl, manager)
//        val netConfig= TradeNetSinkConfig()
//        val dql: Learning<PlankState, Int, DiscreteSpace, IDQN<*>> =
//            QLearningDiscreteDense(mdp, DQNFactoryMultilayerDense(netConfig.conf), netConfig.testQl, manager)

        //start the training
        dql.train()
        manager.save(dql)
        mdp.close()
        checkDqn(dql)
    }

    private fun checkDqn(dql: Learning<PlankState, Int, DiscreteSpace, IDQN<*>>){
        var plankState = PlankState()
        val environmentState = EnvironmentState()
        val visualizer = ShowMePlank(environmentState)
        do {
            environmentState.renderFrame(plankState)
            val input =  dql.getInput(plankState)
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