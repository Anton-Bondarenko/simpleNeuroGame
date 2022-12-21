package ru.bondarenko.neurotest.neuro.mdp.test

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
        val mdp = TestMdp()

        //define the training method
        val dql: Learning<TestState, Int, DiscreteSpace, IDQN<*>> =
            QLearningDiscreteDense(mdp, testNet, testQl, manager)

        //start the training
        dql.train()
        manager.save(dql)
        mdp.close()
        checkDqn(dql)
    }

    private fun checkDqn(dql: Learning<TestState, Int, DiscreteSpace, IDQN<*>>){
        var testState = TestState()
        val environmentState = EnvironmentState()
        val visualizer = ShowMeTest(environmentState)
        do {
            environmentState.renderFrame(testState)
            val input =  dql.getInput(testState)
            val action = dql.neuralNet.output(input)
            if (action.getDouble(0) > action.getDouble(1))
                testState.dec()
            else
                testState.inc()
            visualizer.update(testState)
            if (environmentState.outOfRange(testState))
                testState = TestState()
            Thread.sleep(1_00)
        } while (true)
    }
}