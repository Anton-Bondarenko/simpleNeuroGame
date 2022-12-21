package ru.bondarenko.neurotest.neuro.mdp.test

import org.deeplearning4j.gym.StepReply
import org.deeplearning4j.rl4j.mdp.MDP
import org.deeplearning4j.rl4j.space.DiscreteSpace
import org.deeplearning4j.rl4j.space.ObservationSpace
import org.json.JSONObject

class TestMdp : MDP<TestState, Int, DiscreteSpace> {

    private val actionsSize = 2
    private val rewardAmount = 1.0
    private val environment = EnvironmentState()
    private val visualizer = ShowMeTest(environment)
    private val inputNum = 1

    //    private val observationSpace: ObservationSpace<TestState> = ArrayObservationSpace(IntArray(1))
    override fun getObservationSpace(): ObservationSpace<TestState> {
        return TestObservationSpace(IntArray(1){inputNum})
    }

    private val actionSpace: DiscreteSpace = DiscreteSpace(actionsSize)
    override fun getActionSpace(): DiscreteSpace {
        return actionSpace
    }

    private var testState = TestState()

    override fun reset(): TestState {
        testState = TestState()
        return testState
    }

    override fun close() {}

    override fun isDone(): Boolean {
        return environment.outOfRange(testState)
    }

    override fun newInstance(): MDP<TestState, Int, DiscreteSpace> {
        return TestMdp()
    }

    override fun step(action: Int): StepReply<TestState> {
        when (action) {
            0 -> testState.dec()
            1 -> testState.inc()
        }

        val reward = when {
            environment.outOfRange(testState) -> {
                environment.fails ++
                -1.0
            }else -> 0.0
//                rewardAmount - abs(testState.x) * 0.1
        }

        testState.step++

        environment.renderFrame(testState)
        visualizer.update(testState)

        return StepReply(testState, reward, isDone, JSONObject("{}"))
    }
}